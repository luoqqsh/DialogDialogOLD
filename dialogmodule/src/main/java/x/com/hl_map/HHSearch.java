package x.com.hl_map;

import android.content.Context;

import x.com.hl_map.bean.LocationType;
import x.com.hl_map.bean.MapType;
import x.com.hl_map.listener.OnLocationListener;

public class HHSearch {
    private SearchController searchController;
    private MapType mapType;
    private Context context;

    public HHSearch(Context context, MapType mapType, SearchController searchController) {
        this.searchController = searchController;
        this.mapType = mapType;
        this.context = context;
    }

    public void address2Point(String address, String cityCode, OnLocationListener onLocationListener) {
        searchController.address2Point(context, address, cityCode, onLocationListener);
    }

    public void point2address(double lat, double lng, float scope, LocationType type, OnLocationListener onLocationListener) {
        searchController.point2address(context, lat, lng, scope, type, onLocationListener);
    }

    public void showTips(String address, String cityCode, boolean isCityLimit, OnLocationListener onLocationListener) {
        searchController.showTips(context, address, cityCode, isCityLimit, onLocationListener);
    }

    public void poiSearch(String keyword, String type, String cityCode, OnLocationListener onLocationListener) {
        poiSearch(keyword, type, cityCode, 1, 10, onLocationListener);
    }

    public void poiSearch(String keyword, String type, String cityCode, int page, int pagesize, OnLocationListener onLocationListener) {
        searchController.poiSearch(context, keyword, type, cityCode, page, pagesize, onLocationListener);
    }
}
