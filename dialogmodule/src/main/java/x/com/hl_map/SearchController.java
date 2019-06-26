package x.com.hl_map;

import x.com.hl_map.bean.LocationBean;

public abstract class SearchController {
    public abstract LocationBean addressToPoint(String address);

    public abstract LocationBean addressToPoint(double lat, double lng);

}
