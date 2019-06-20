package x.com.hl_map;

import x.com.hl_map.bean.AddressBean;
import x.com.hl_map.bean.LatLngs;

public abstract class SearchController {
    public abstract LatLngs addressToPoint(String address);

    public abstract AddressBean addressToPoint(double lat, double lng);

}
