package x.com.hl_rx.bean;

import java.util.List;

/**
 * copyright：
 * author：anthui
 * Version：1.0
 * creation date： 2019/10/18.
 * describe：
 */
public class HomeDataBean {


    /**
     * userGolds : [{"id":118,"userId":32,"type":1,"step":0,"gold":6,"state":0,"takeDate":"2019-10-21 00:00:00","takeTime":null,"createdAt":"2019-10-21 11:15:21","updatedAt":"2019-10-21 14:47:36"},{"id":120,"userId":32,"type":3,"step":0,"gold":22,"state":0,"takeDate":"2019-10-21 00:00:00","takeTime":null,"createdAt":"2019-10-21 11:15:21","updatedAt":"2019-10-21 15:05:37"}]
     * gold : 257
     * goldMoney : 0.03
     * drawType : 0
     * panelDatas : [{"id":null,"value":"1,10","code":"first_stage_steps_gold_config"},{"id":null,"value":"1500,80","code":"second_stage_steps_gold_config"},{"id":null,"value":"3000,100","code":"three_stage_steps_gold_config"},{"id":null,"value":"4500,120","code":"four_stage_steps_gold_config"},{"id":null,"value":"6000,200","code":"five_stage_steps_gold_config"}]
     * currentDate : 2019-10-21 15:05:51
     * stageContent : 满1步领10金币
     * maxSteps : 20000
     * stepCount : 0
     * totalUseStep : 0
     * stepsGoldVideoConfig : 2000
     */

    private String scaleCode;
    private int gold;
    //0弹，1不谈，防止 默认值弹窗
    private int isSameDeviceLogin = -1;
    //是否强制 同步后台数据
    private int localFlush = 0;
    private double goldMoney;
    private int drawType;
    private int stepBtnType;
    private int nextScaleStep;
    private int currentScaleStep;
    private String currentScaleStepContent;
    private String currentDate;
    private String stageContent;
    private int maxSteps;
    private int redPacketFlag = -1;
    private int stepCount;
    private int totalUseStep;
    private int stepsGoldVideoConfig;
    private int hasStepsGold;
    private List<UserGoldsBean> goldBubbleVos;
    private List<PanelDatasBean> panelDatas;

    public int getNextScaleStep() {
        return nextScaleStep;
    }

    public int getLocalFlush() {
        return localFlush;
    }

    public int getIsSameDeviceLogin() {
        return isSameDeviceLogin;
    }

    public void setIsSameDeviceLogin(int isSameDeviceLogin) {
        this.isSameDeviceLogin = isSameDeviceLogin;
    }

    public int getRedPacketFlag() {
        return redPacketFlag;
    }

    public String getScaleCode() {
        return scaleCode;
    }

    public void setScaleCode(String scaleCode) {
        this.scaleCode = scaleCode;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public double getGoldMoney() {
        return goldMoney;
    }

    public void setGoldMoney(double goldMoney) {
        this.goldMoney = goldMoney;
    }

    public int getDrawType() {
        return drawType;
    }

    public void setDrawType(int drawType) {
        this.drawType = drawType;
    }

    public int getStepBtnType() {
        return stepBtnType;
    }

    public void setStepBtnType(int stepBtnType) {
        this.stepBtnType = stepBtnType;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getStageContent() {
        return stageContent;
    }

    public void setStageContent(String stageContent) {
        this.stageContent = stageContent;
    }

    public int getCurrentScaleStep() {
        return currentScaleStep;
    }

    public void setCurrentScaleStep(int currentScaleStep) {
        this.currentScaleStep = currentScaleStep;
    }

    public String getCurrentScaleStepContent() {
        return currentScaleStepContent;
    }

    public void setCurrentScaleStepContent(String currentScaleStepContent) {
        this.currentScaleStepContent = currentScaleStepContent;
    }

    public int getMaxSteps() {
        return maxSteps;
    }

    public void setMaxSteps(int maxSteps) {
        this.maxSteps = maxSteps;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public int getTotalUseStep() {
        return totalUseStep;
    }

    public void setTotalUseStep(int totalUseStep) {
        this.totalUseStep = totalUseStep;
    }

    public int getStepsGoldVideoConfig() {
        return stepsGoldVideoConfig;
    }

    public void setStepsGoldVideoConfig(int stepsGoldVideoConfig) {
        this.stepsGoldVideoConfig = stepsGoldVideoConfig;
    }

    public List<UserGoldsBean> getUserGolds() {
        return goldBubbleVos;
    }

    public void setUserGolds(List<UserGoldsBean> userGolds) {
        this.goldBubbleVos = userGolds;
    }

    public int getHasStepsGold() {
        return hasStepsGold;
    }

    public void setHasStepsGold(int hasStepsGold) {
        this.hasStepsGold = hasStepsGold;
    }

    public List<PanelDatasBean> getPanelDatas() {
        return panelDatas;
    }

    public void setPanelDatas(List<PanelDatasBean> panelDatas) {
        this.panelDatas = panelDatas;
    }


    public static class PanelDatasBean {
        /**
         * id : null
         * value : 1,10
         * code : first_stage_steps_gold_config
         */

        private Object id;
        private String value;
        private int scale;
        private String gold;
        private String code;

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {

            this.code = code;
        }

        public int getScale() {
            return scale;
        }

        public void setScale(int scale) {
            this.scale = scale;
        }

        public String getGold() {
            return gold;
        }

        public void setGold(String gold) {
            this.gold = gold;
        }
    }


}
