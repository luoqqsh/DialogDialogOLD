package x.com.hl_rx.bean;


/**
 * copyright：
 * author：anthui
 * Version：1.0
 * creation date： 2019/12/25.
 * describe：
 */
public class UserGoldsBean {
    /**
     * id : 11718
     * userId : 1177
     * type : 1
     * step : 0
     * gold : 4
     * takeDate : 2020-01-03 00:00:00
     * takeTime : null
     * everyDayTaskVo : null
     * receiveNum : 0
     * state : 0
     * show : 1
     * name : 一号幸
     * imgUrl : http://steps.ss.bscstorage.com/dev-img-steps/20191226/MSVGA1kR5AXV4G1djjWoOypNkBsTfisaygb73t8e.png
     * fbStatus : 1
     * xyAmountMin : 1
     * xyAmountMax : 10
     * imgUrlNo : www.xxx
     * sjCountMinI : 1
     * sjCountMaxI : 14
     * sjAmountMinI : 1
     * sjAmountMaxI : 10
     * taskCode : targetTask
     * rwDj : 1
     * ljUrl : null
     * ljDj : 1
     * isDefault : 1
     * refreshTime : null
     * sjTimeI : 5
     * sjCountMinIi : 15
     * sjCountMaxIi : 30
     * sjTimeIi : 15
     * sjCountMinIii : 30
     * sjTimeIii : 60
     * currentDate : null
     * xyTime : 5
     * sjAmountMinIi : 1
     * sjAmountMaxIi : 5
     * sjAmountMinIii : 1
     * sjAmountMaxIii : 10
     * bucket : 1
     * force : 1
     * delayTime : 1578041594042
     * replaceTime : 1578041770042
     * countdownTime : 1578041680042
     * serverTime : 1578041594042
     * delayTimeMin : 0
     * delayTimeMax : 0
     * replaceTimeMin : 1
     * replaceTimeMax : 100
     * countdownTimeMin : 10
     * countdownTimeMax : 100
     * showGoldTag :0
     */

    private int id;
    private int userId;
    private int type;
    private int step;
    private int gold;
    private String takeDate;
    private String takeTime;
    private WorksDayData everyDayTaskVo;
    private int receiveNum;
    private int state;
    private int show;
    private String name;
    private String imgUrl;
    private int fbStatus;
    private int xyAmountMin;
    private int xyAmountMax;
    private String imgUrlNo;
    private int sjCountMinI;
    private int sjCountMaxI;
    private int sjAmountMinI;
    private int sjAmountMaxI;
    private String taskCode;
    private int rwDj;
    private String ljUrl;
    private int ljDj;
    private int isDefault;
    private String refreshTime;
    private int sjTimeI;
    private int sjCountMinIi;
    private int sjCountMaxIi;
    private int sjTimeIi;
    private int sjCountMinIii;
    private int sjTimeIii;
    private String currentDate;
    private int xyTime;
    private int sjAmountMinIi;
    private int sjAmountMaxIi;
    private int sjAmountMinIii;
    private int sjAmountMaxIii;
    private int bucket;
    private int force;
    private long delayTime;
    private long replaceTime;
    private long countdownTime;
    private long serverTime;
    private int delayTimeMin;
    private int delayTimeMax;
    private int replaceTimeMin;
    private int replaceTimeMax;
    private int countdownTimeMin;
    private int countdownTimeMax;
    /**是否显示金币数量的标识(0否1是)*/
    private int showGoldTag;

    public int getShowGoldTag() {
        return showGoldTag;
    }

    public void setShowGoldTag(int showGoldTag) {
        this.showGoldTag = showGoldTag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public String getTakeDate() {
        return takeDate;
    }

    public void setTakeDate(String takeDate) {
        this.takeDate = takeDate;
    }

    public String getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(String takeTime) {
        this.takeTime = takeTime;
    }

    public WorksDayData getEveryDayTaskVo() {
        return everyDayTaskVo;
    }

    public void setEveryDayTaskVo(WorksDayData everyDayTaskVo) {
        this.everyDayTaskVo = everyDayTaskVo;
    }

    public int getReceiveNum() {
        return receiveNum;
    }

    public void setReceiveNum(int receiveNum) {
        this.receiveNum = receiveNum;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean getShow() {
        return show == 1;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean getFbStatus() {
        return fbStatus == 1;
    }

    public void setFbStatus(int fbStatus) {
        this.fbStatus = fbStatus;
    }

    public int getXyAmountMin() {
        return xyAmountMin;
    }

    public void setXyAmountMin(int xyAmountMin) {
        this.xyAmountMin = xyAmountMin;
    }

    public int getXyAmountMax() {
        return xyAmountMax;
    }

    public void setXyAmountMax(int xyAmountMax) {
        this.xyAmountMax = xyAmountMax;
    }

    public String getImgUrlNo() {
        return imgUrlNo;
    }

    public void setImgUrlNo(String imgUrlNo) {
        this.imgUrlNo = imgUrlNo;
    }

    public int getSjCountMinI() {
        return sjCountMinI;
    }

    public void setSjCountMinI(int sjCountMinI) {
        this.sjCountMinI = sjCountMinI;
    }

    public int getSjCountMaxI() {
        return sjCountMaxI;
    }

    public void setSjCountMaxI(int sjCountMaxI) {
        this.sjCountMaxI = sjCountMaxI;
    }

    public int getSjAmountMinI() {
        return sjAmountMinI;
    }

    public void setSjAmountMinI(int sjAmountMinI) {
        this.sjAmountMinI = sjAmountMinI;
    }

    public int getSjAmountMaxI() {
        return sjAmountMaxI;
    }

    public void setSjAmountMaxI(int sjAmountMaxI) {
        this.sjAmountMaxI = sjAmountMaxI;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public int getRwDj() {
        return rwDj;
    }

    public void setRwDj(int rwDj) {
        this.rwDj = rwDj;
    }

    public String getLjUrl() {
        return ljUrl;
    }

    public void setLjUrl(String ljUrl) {
        this.ljUrl = ljUrl;
    }

    public int getLjDj() {
        return ljDj;
    }

    public void setLjDj(int ljDj) {
        this.ljDj = ljDj;
    }

    public boolean getIsDefault() {
        return isDefault==1;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public String getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(String refreshTime) {
        this.refreshTime = refreshTime;
    }

    public int getSjTimeI() {
        return sjTimeI;
    }

    public void setSjTimeI(int sjTimeI) {
        this.sjTimeI = sjTimeI;
    }

    public int getSjCountMinIi() {
        return sjCountMinIi;
    }

    public void setSjCountMinIi(int sjCountMinIi) {
        this.sjCountMinIi = sjCountMinIi;
    }

    public int getSjCountMaxIi() {
        return sjCountMaxIi;
    }

    public void setSjCountMaxIi(int sjCountMaxIi) {
        this.sjCountMaxIi = sjCountMaxIi;
    }

    public int getSjTimeIi() {
        return sjTimeIi;
    }

    public void setSjTimeIi(int sjTimeIi) {
        this.sjTimeIi = sjTimeIi;
    }

    public int getSjCountMinIii() {
        return sjCountMinIii;
    }

    public void setSjCountMinIii(int sjCountMinIii) {
        this.sjCountMinIii = sjCountMinIii;
    }

    public int getSjTimeIii() {
        return sjTimeIii;
    }

    public void setSjTimeIii(int sjTimeIii) {
        this.sjTimeIii = sjTimeIii;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public int getXyTime() {
        return xyTime;
    }

    public void setXyTime(int xyTime) {
        this.xyTime = xyTime;
    }

    public int getSjAmountMinIi() {
        return sjAmountMinIi;
    }

    public void setSjAmountMinIi(int sjAmountMinIi) {
        this.sjAmountMinIi = sjAmountMinIi;
    }

    public int getSjAmountMaxIi() {
        return sjAmountMaxIi;
    }

    public void setSjAmountMaxIi(int sjAmountMaxIi) {
        this.sjAmountMaxIi = sjAmountMaxIi;
    }

    public int getSjAmountMinIii() {
        return sjAmountMinIii;
    }

    public void setSjAmountMinIii(int sjAmountMinIii) {
        this.sjAmountMinIii = sjAmountMinIii;
    }

    public int getSjAmountMaxIii() {
        return sjAmountMaxIii;
    }

    public void setSjAmountMaxIii(int sjAmountMaxIii) {
        this.sjAmountMaxIii = sjAmountMaxIii;
    }

    public int getBucket() {
        return bucket;
    }

    public void setBucket(int bucket) {
        this.bucket = bucket;
    }

    public int getForce() {
        return force;
    }

    public void setForce(int force) {
        this.force = force;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    public long getReplaceTime() {
        return replaceTime;
    }

    public void setReplaceTime(long replaceTime) {
        this.replaceTime = replaceTime;
    }

    public long getCountdownTime() {
        return countdownTime;
    }

    public void setCountdownTime(long countdownTime) {
        this.countdownTime = countdownTime;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public int getDelayTimeMin() {
        return delayTimeMin;
    }

    public void setDelayTimeMin(int delayTimeMin) {
        this.delayTimeMin = delayTimeMin;
    }

    public int getDelayTimeMax() {
        return delayTimeMax;
    }

    public void setDelayTimeMax(int delayTimeMax) {
        this.delayTimeMax = delayTimeMax;
    }

    public int getReplaceTimeMin() {
        return replaceTimeMin;
    }

    public void setReplaceTimeMin(int replaceTimeMin) {
        this.replaceTimeMin = replaceTimeMin;
    }

    public int getReplaceTimeMax() {
        return replaceTimeMax;
    }

    public void setReplaceTimeMax(int replaceTimeMax) {
        this.replaceTimeMax = replaceTimeMax;
    }

    public int getCountdownTimeMin() {
        return countdownTimeMin;
    }

    public void setCountdownTimeMin(int countdownTimeMin) {
        this.countdownTimeMin = countdownTimeMin;
    }

    public int getCountdownTimeMax() {
        return countdownTimeMax;
    }

    public void setCountdownTimeMax(int countdownTimeMax) {
        this.countdownTimeMax = countdownTimeMax;
    }


//
//    /**
//     * id : 6158
//     * userId : 1092
//     * type : 6
//     * step : 0
//     * gold : 10
//     * takeDate : 2019-12-25 00:00:00
//     * takeTime : 2019-12-25 14:47:28
//     * everyDayTaskVo : null
//     * show : 1
//     * name : 链接气泡
//     * imgUrl : cwf
//     * fbStatus : 1
//     * xyAmountMin : 1
//     * xyAmountMax : 10
//     * imgUrlNo : null
//     * sjCountMinI : 1
//     * sjCountMaxI : 14
//     * sjAmountMinI : null
//     * sjAmountMaxI : null
//     * taskCode : targetTask
//     * rwDj : null
//     * ljUrl : https://abc.com
//     * ljDj : 10
//     */
//
//    private int id;
//    private int userId;
//    private int type;
//    private int step;
//    private int gold;
//    private int state;
//
//    private String takeDate;
//    private String refreshTime;
//    private String currentDate;
//    private String takeTime;
//    private WorksDayData everyDayTaskVo;
//    private int show;
//    private String name;
//    private String imgUrl;
//    private int fbStatus = 0;
//    private int xyAmountMin;
//    private int xyAmountMax;
//    private String imgUrlNo;
//    private int sjCountMinI;
//    private int sjCountMaxI;
//    private int isDefault;
//    private String sjAmountMinI;
//    private String sjAmountMaxI;
//    private String taskCode;
//    private String rwDj;
//    private String ljUrl;
//    private int ljDj;
//
//    public int getState() {
//        return state;
//    }
//
//    public String getCurrentDate() {
//        return currentDate;
//    }
//
//    public String getRefreshTime() {
//        return refreshTime;
//    }
//
//    public boolean getIsDefault() {
//        return isDefault == 1;
//    }
//
//    public WorksDayData getGoldBubbleVos() {
//        return everyDayTaskVo;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }
//
//    public int getType() {
//        return type;
//    }
//
//    public void setType(int type) {
//        this.type = type;
//    }
//
//    public int getStep() {
//        return step;
//    }
//
//    public void setStep(int step) {
//        this.step = step;
//    }
//
//    public int getGold() {
//        return gold;
//    }
//
//    public void setGold(int gold) {
//        this.gold = gold;
//    }
//
//    public String getTakeDate() {
//        return takeDate;
//    }
//
//    public void setTakeDate(String takeDate) {
//        this.takeDate = takeDate;
//    }
//
//    public String getTakeTime() {
//        return takeTime;
//    }
//
//    public void setTakeTime(String takeTime) {
//        this.takeTime = takeTime;
//    }
//
//
//    public boolean getShow() {
//        return show == 1;
//    }
//
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getImgUrl() {
//        return imgUrl;
//    }
//
//    public void setImgUrl(String imgUrl) {
//        this.imgUrl = imgUrl;
//    }
//
//    public boolean getFbStatus() {
//        return fbStatus == 1;
//    }
//
//
//    public int getXyAmountMin() {
//        return xyAmountMin;
//    }
//
//    public void setXyAmountMin(int xyAmountMin) {
//        this.xyAmountMin = xyAmountMin;
//    }
//
//    public int getXyAmountMax() {
//        return xyAmountMax;
//    }
//
//    public void setXyAmountMax(int xyAmountMax) {
//        this.xyAmountMax = xyAmountMax;
//    }
//
//    public String getImgUrlNo() {
//        return imgUrlNo;
//    }
//
//    public void setImgUrlNo(String imgUrlNo) {
//        this.imgUrlNo = imgUrlNo;
//    }
//
//    public int getSjCountMinI() {
//        return sjCountMinI;
//    }
//
//    public void setSjCountMinI(int sjCountMinI) {
//        this.sjCountMinI = sjCountMinI;
//    }
//
//    public int getSjCountMaxI() {
//        return sjCountMaxI;
//    }
//
//    public void setSjCountMaxI(int sjCountMaxI) {
//        this.sjCountMaxI = sjCountMaxI;
//    }
//
//    public String getSjAmountMinI() {
//        return sjAmountMinI;
//    }
//
//    public void setSjAmountMinI(String sjAmountMinI) {
//        this.sjAmountMinI = sjAmountMinI;
//    }
//
//    public String getSjAmountMaxI() {
//        return sjAmountMaxI;
//    }
//
//    public void setSjAmountMaxI(String sjAmountMaxI) {
//        this.sjAmountMaxI = sjAmountMaxI;
//    }
//
//    public String getTaskCode() {
//        return taskCode;
//    }
//
//    public void setTaskCode(String taskCode) {
//        this.taskCode = taskCode;
//    }
//
//    public String getRwDj() {
//        return rwDj;
//    }
//
//    public void setRwDj(String rwDj) {
//        this.rwDj = rwDj;
//    }
//
//    public String getLjUrl() {
//        return ljUrl;
//    }
//
//    public void setLjUrl(String ljUrl) {
//        this.ljUrl = ljUrl;
//    }
//
//    public int getLjDj() {
//        return ljDj;
//    }
//
//    public void setLjDj(int ljDj) {
//
//
//        this.ljDj = ljDj;
//    }


}
