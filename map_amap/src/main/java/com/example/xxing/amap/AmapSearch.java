package com.example.xxing.amap;

import android.content.Context;
import android.text.TextUtils;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.ArrayList;
import java.util.List;

import x.com.hl_log.ViseLog;
import x.com.hl_map.SearchController;
import x.com.hl_map.bean.ErrorType;
import x.com.hl_map.bean.LatLngBean;
import x.com.hl_map.bean.LocationBean;
import x.com.hl_map.bean.LocationFrom;
import x.com.hl_map.bean.LocationType;
import x.com.hl_map.listener.OnLocationListener;

public class AmapSearch extends SearchController {
    @Override
    public void address2Point(Context context, String address, String cityCode, final OnLocationListener onLocationListener) {
        if (onLocationListener == null) return;
        if (TextUtils.isEmpty(address) || TextUtils.isEmpty(cityCode)) {
            onLocationListener.onError(ErrorType.NULL.getCode(), ErrorType.NULL.getValue());
            return;
        }
        GeocodeSearch geocodeSearch = new GeocodeSearch(context);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                if (i == 1000) {
                    List<GeocodeAddress> geocodeAddressList = geocodeResult.getGeocodeAddressList();
                    if (geocodeAddressList.size() > 0) {
                        LocationBean locationBean = new LocationBean(LocationFrom.ADDRESS2POINT,
                                new LatLngBean(geocodeAddressList.get(0).getLatLonPoint().getLatitude(),
                                        geocodeAddressList.get(0).getLatLonPoint().getLongitude()));
                        locationBean.setAddress(geocodeAddressList.get(0).getFormatAddress());
                        onLocationListener.onCurrentLocation(locationBean);
                    } else {
                        onLocationListener.onError(ErrorType.NODATA.getCode(), ErrorType.NODATA.getValue());
                    }
                } else {
                    onLocationListener.onError(ErrorType.OTHER.getCode(), ErrorType.NODATA.getValue());
                }
            }
        });
        GeocodeQuery query = new GeocodeQuery(address, cityCode);
        geocodeSearch.getFromLocationNameAsyn(query);
    }

    @Override
    public void point2address(Context context, final double lat, double lng, float scope, LocationType type, final OnLocationListener onLocationListener) {
        if (onLocationListener == null) return;
        if (lat == 0 || lng == 0) {
            onLocationListener.onError(ErrorType.LATLNGERROR.getCode(), ErrorType.LATLNGERROR.getValue());
            return;
        }
        GeocodeSearch geocodeSearch = new GeocodeSearch(context);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                if (i == 1000) {
                    LocationBean locationBean = new LocationBean(LocationFrom.POINT2ADDRESS, new LatLngBean(regeocodeResult.getRegeocodeQuery().getPoint().getLatitude(),
                            regeocodeResult.getRegeocodeQuery().getPoint().getLongitude()));
                    locationBean.setAddress(regeocodeResult.getRegeocodeAddress().getFormatAddress());
                    List<PoiItem> items = regeocodeResult.getRegeocodeAddress().getPois();
                    if (items.size() > 0) {
                        List<LatLngBean> latLngBeans = new ArrayList<>();
                        for (int j = 0; j < items.size(); j++) {
                            latLngBeans.add(new LatLngBean(items.get(j).getLatLonPoint().getLatitude(), items.get(j).getLatLonPoint().getLongitude(),items.get(j).getAdName()));
                        }
                        locationBean.setPois(latLngBeans);
                        onLocationListener.onCurrentLocation(locationBean);
                    } else {
                        onLocationListener.onError(ErrorType.NODATA.getCode(), ErrorType.NODATA.getValue());
                    }
                } else {
                    onLocationListener.onError(ErrorType.OTHER.getCode(), ErrorType.OTHER.getValue());
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                ViseLog.d(geocodeResult);
            }
        });
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(lat, lng), scope, type.getType());
        geocodeSearch.getFromLocationAsyn(query);
    }

    @Override
    public void poiSearch(Context context, String keyword, String type, String cityCode, int page, int pagesize, final OnLocationListener onLocationListener) {
        if (onLocationListener == null) return;
        if (TextUtils.isEmpty(keyword) && TextUtils.isEmpty(type)) {
            onLocationListener.onError(ErrorType.NULL.getCode(), ErrorType.NULL.getValue());
            return;
        }
        PoiSearch.Query query = new PoiSearch.Query(keyword, type, cityCode);
        query.setPageSize(pagesize);// 设置每页最多返回多少条poiitem
        query.setPageNum(page);//设置查询页码
        final PoiSearch poiSearch = new PoiSearch(context, query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                if (i == 1000) {
                    LocationBean locationBean = new LocationBean(LocationFrom.POINT2ADDRESS, null);
                    List<PoiItem> items = poiResult.getPois();
                    if (items.size() > 0) {
                        List<LatLngBean> latLngBeans = new ArrayList<>();
                        for (int j = 0; j < items.size(); j++) {
                            latLngBeans.add(new LatLngBean(items.get(j).getLatLonPoint().getLatitude(), items.get(j).getLatLonPoint().getLongitude(),items.get(j).getTitle()));
                        }
                        locationBean.setPois(latLngBeans);
                        onLocationListener.onCurrentLocation(locationBean);
                    } else {
                        onLocationListener.onError(ErrorType.NODATA.getCode(), ErrorType.NODATA.getValue());
                    }
                } else {
                    onLocationListener.onError(ErrorType.OTHER.getCode(), ErrorType.OTHER.getValue());
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {
            }
        });
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void showTips(Context context, String address, String cityCode, boolean isCityLimit, final OnLocationListener onLocationListener) {
        if (onLocationListener == null) return;
        if (TextUtils.isEmpty(address) || TextUtils.isEmpty(cityCode)) {
            onLocationListener.onError(ErrorType.NULL.getCode(), ErrorType.NULL.getValue());
            return;
        }
        InputtipsQuery inputquery = new InputtipsQuery(address, cityCode);
        inputquery.setCityLimit(isCityLimit);
        Inputtips inputTips = new Inputtips(context, inputquery);
        inputTips.setInputtipsListener(new Inputtips.InputtipsListener() {
            @Override
            public void onGetInputtips(List<Tip> list, int i) {
                if (list.size() > 0) {
                    LocationBean locationBean = new LocationBean(LocationFrom.SHOWTIPS, null);
                    List<LatLngBean> latLngBeans = new ArrayList<>();
                    for (int j = 0; j < list.size(); j++) {
                        latLngBeans.add(new LatLngBean(list.get(j).getPoint().getLatitude(), list.get(j).getPoint().getLongitude(), list.get(j).getName()));
                    }
                    locationBean.setPois(latLngBeans);
                    onLocationListener.onCurrentLocation(locationBean);
                } else {
                    onLocationListener.onError(ErrorType.NODATA.getCode(), ErrorType.NODATA.getValue());
                }
            }
        });
        inputTips.requestInputtipsAsyn();
    }
}