package com.example.xxing.map_bamap;


import android.content.Context;
import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

import java.util.ArrayList;
import java.util.List;

import x.com.hl_log.ViseLog;
import x.com.hl_map.SearchController;
import x.com.hl_map.bean.ErrorType;
import x.com.hl_map.bean.LatLngBean;
import x.com.hl_map.bean.LocationBean;
import x.com.hl_map.bean.LocationFrom;
import x.com.hl_map.bean.LocationType;
import x.com.hl_map.bean.MapType;
import x.com.hl_map.listener.OnLocationListener;

public class BmapSearch extends SearchController {
    @Override
    public void address2Point(Context context, String address, String cityCode, final OnLocationListener onLocationListener) {
        if (onLocationListener == null) return;
        if (TextUtils.isEmpty(address) || TextUtils.isEmpty(cityCode)) {
            onLocationListener.onError(ErrorType.NULL.getCode(), ErrorType.NULL.getValue());
            return;
        }
        GeoCodeOption query = new GeoCodeOption();
        query.address(address).city(cityCode);
        final GeoCoder mCoder = GeoCoder.newInstance();
        mCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                ViseLog.d(geoCodeResult);
                if (geoCodeResult.error == SearchResult.ERRORNO.NO_ERROR) {
                    LocationBean locationBean = new LocationBean(MapType.BAIDUMAP,LocationFrom.ADDRESS2POINT,
                            new LatLngBean(geoCodeResult.getLocation().latitude,
                                    geoCodeResult.getLocation().latitude));
                    locationBean.setAddress(geoCodeResult.getAddress());
                    onLocationListener.onCurrentLocation(locationBean);
                } else {
                    onLocationListener.onError(ErrorType.OTHER.getCode(), ErrorType.NODATA.getValue());
                }
                mCoder.destroy();
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

            }
        });
        mCoder.geocode(query);
    }

    @Override
    public void point2address(Context context, final double lat, double lng, float scope, LocationType type, final OnLocationListener onLocationListener) {
        if (onLocationListener == null) return;
        if (lat == 0 || lng == 0) {
            onLocationListener.onError(ErrorType.LATLNGERROR.getCode(), ErrorType.LATLNGERROR.getValue());
            return;
        }
        ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption();
        reverseGeoCodeOption.location(new LatLng(lat, lng)).radius((int) scope);
        // POI召回半径，允许设置区间为0-1000米，超过1000米按1000米召回。默认值为1000
        final GeoCoder mCoder = GeoCoder.newInstance();
        mCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                ViseLog.d(reverseGeoCodeResult);
                if (reverseGeoCodeResult.error == SearchResult.ERRORNO.NO_ERROR) {
                    LocationBean locationBean = new LocationBean(MapType.BAIDUMAP,LocationFrom.POINT2ADDRESS, new LatLngBean(reverseGeoCodeResult.getLocation().latitude,
                            reverseGeoCodeResult.getLocation().latitude));
                    locationBean.setAddress(reverseGeoCodeResult.getAddress());
                    List<PoiInfo> items = reverseGeoCodeResult.getPoiList();
                    if (items != null && items.size() > 0) {
                        List<LatLngBean> latLngBeans = new ArrayList<>();
                        for (int j = 0; j < items.size(); j++) {
                            latLngBeans.add(new LatLngBean(items.get(j).getLocation().latitude, items.get(j).getLocation().longitude, items.get(j).getAddress()));
                        }
                        locationBean.setPois(latLngBeans);
                        onLocationListener.onCurrentLocation(locationBean);
                    } else {
                        onLocationListener.onError(ErrorType.NODATA.getCode(), ErrorType.NODATA.getValue());
                    }
                } else {
                    onLocationListener.onError(ErrorType.OTHER.getCode(), ErrorType.OTHER.getValue());
                }
                mCoder.destroy();
            }
        });
        mCoder.reverseGeoCode(reverseGeoCodeOption);
    }

    @Override
    public void poiSearch(Context context, String keyword, String type, String cityCode, int page, int pagesize, final OnLocationListener onLocationListener) {
        if (onLocationListener == null) return;
        if (TextUtils.isEmpty(keyword) && TextUtils.isEmpty(type)) {
            onLocationListener.onError(ErrorType.NULL.getCode(), ErrorType.NULL.getValue());
            return;
        }
        PoiCitySearchOption poiCitySearchOption = new PoiCitySearchOption();
        poiCitySearchOption.city(cityCode).keyword(keyword).pageNum(10).cityLimit(true);
        PoiSearch mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                ViseLog.d(poiResult);
                if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
                    LocationBean locationBean = new LocationBean(MapType.BAIDUMAP,LocationFrom.POINT2ADDRESS, null);
                    List<PoiInfo> items = poiResult.getAllPoi();
                    if (items.size() > 0) {
                        List<LatLngBean> latLngBeans = new ArrayList<>();
                        for (int j = 0; j < items.size(); j++) {
                            latLngBeans.add(new LatLngBean(items.get(j).getLocation().latitude, items.get(j).getLocation().longitude, items.get(j).getName()));
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
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                ViseLog.d(poiDetailResult);
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
                ViseLog.d(poiDetailSearchResult);

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
                ViseLog.d(poiIndoorResult);
            }
        });
        mPoiSearch.searchInCity(poiCitySearchOption);
    }

    @Override
    public void showTips(Context context, String address, String cityCode, boolean isCityLimit, final OnLocationListener onLocationListener) {
        if (onLocationListener == null) return;
        if (TextUtils.isEmpty(address) || TextUtils.isEmpty(cityCode)) {
            onLocationListener.onError(ErrorType.NULL.getCode(), ErrorType.NULL.getValue());
            return;
        }
        SuggestionSearchOption suggestionSearchOption = new SuggestionSearchOption();
        suggestionSearchOption.city(cityCode).keyword(address).citylimit(isCityLimit);
        final SuggestionSearch mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                if (suggestionResult.error == SearchResult.ERRORNO.NO_ERROR) {
                    if (suggestionResult.getAllSuggestions().size() > 0) {
                        LocationBean locationBean = new LocationBean(MapType.BAIDUMAP,LocationFrom.SHOWTIPS, null);
                        List<LatLngBean> latLngBeans = new ArrayList<>();
                        for (int j = 0; j < suggestionResult.getAllSuggestions().size(); j++) {
                            latLngBeans.add(new LatLngBean(suggestionResult.getAllSuggestions().get(j).getPt().latitude, suggestionResult.getAllSuggestions().get(j).getPt().longitude, suggestionResult.getAllSuggestions().get(j).getKey()));
                        }
                        locationBean.setPois(latLngBeans);
                        onLocationListener.onCurrentLocation(locationBean);
                    } else {
                        onLocationListener.onError(ErrorType.NODATA.getCode(), ErrorType.NODATA.getValue());
                    }
                } else if (suggestionResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    onLocationListener.onError(ErrorType.NODATA.getCode(), ErrorType.NODATA.getValue());
                }
                mSuggestionSearch.destroy();
            }
        });
        mSuggestionSearch.requestSuggestion(suggestionSearchOption);
    }
}