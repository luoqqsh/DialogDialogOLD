package x.com.dialogdialog.face.model;

public class OnLineTokenResponse {

    /**
     * refresh_token : 25.e874911b9f3504103fcf6e85e681d6aa.315360000.1882831911.282335-17162119
     * expires_in : 2592000
     * session_key : 9mzdWEmdjgnNAKrtj6VjzsWa/CbVZ8uidBwrvjaok5l/jLLEK/lYNri5Uf+49V4cqXibQmAfdZwrRTdu43DaSLVZoUzLWQ==
     * access_token : 24.c97f45573425da706dedf12b4d049e1a.2592000.1570063911.282335-17162119
     * scope : public vis-ocr_ocr vis-classify_dishes brain_ocr_scope vis-classify_car brain_ocr_general brain_ocr_general_basic vis-ocr_business_license brain_ocr_webimage brain_all_scope brain_ocr_idcard brain_ocr_driving_license brain_ocr_vehicle_license vis-ocr_plate_number vis-classify_animal vis-classify_plant brain_solution brain_ocr_plate_number brain_ocr_accurate brain_ocr_accurate_basic brain_ocr_receipt brain_ocr_business_license brain_object_detect brain_realtime_logo brain_dish_detect brain_car_detect brain_animal_classify brain_plant_classify brain_solution_iocr brain_ingredient vis-faceverify_faceverify_h5-face-liveness brain_ocr_handwriting brain_body_analysis brain_body_attr brain_ocr_passport brain_ocr_vat_invoice brain_advanced_general_classify brain_custom_dish brain_numbers brain_body_number vis-faceverify_FACE_V3 brain_image_quality_enhance brain_driver_behavior brain_body_seg brain_contrast_enhance brain_dehaze brain_ocr_train_ticket brain_ocr_taxi_receipt brain_gesture_detect vis-ocr_household_register vis-ocr_vis-classify_birth_certificate brain_poi_recognize vis-ocr_台湾通行证 vis-ocr_港澳通行证 brain_body_tracking vis-ocr_车辆vin码识别 vis-ocr_定额发票识别 brain_vehicle_detect brain_redwine brain_colourize brain_ocr_vin brain_ocr_quota_invoice vis-faceverify_idl_face_merge brain_stretch_restore brain_ocr_birth_certificate brain_ocr_household_register brain_ocr_HK_Macau_pass brain_ocr_taiwan_pass brain_currency brain_vehicle_damage brain_cvpaas-app-scope wise_adapt lebo_resource_base lightservice_public hetu_basic lightcms_map_poi kaidian_kaidian ApsMisTest_Test权限 vis-classify_flower lpq_开放 cop_helloScope ApsMis_fangdi_permission smartapp_snsapi_base iop_autocar oauth_tp_app smartapp_smart_game_openapi oauth_sessionkey smartapp_swanid_verify smartapp_opensource_openapi smartapp_opensource_recapi fake_face_detect_开放Scope
     * session_secret : 733571999110624e296cf102564f79e1
     */

    private String refresh_token;
    private String expires_in;
    private String session_key;
    private String access_token;
    private String scope;
    private String session_secret;

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getSession_secret() {
        return session_secret;
    }

    public void setSession_secret(String session_secret) {
        this.session_secret = session_secret;
    }
}
