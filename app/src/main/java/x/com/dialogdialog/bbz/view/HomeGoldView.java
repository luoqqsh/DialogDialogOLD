package x.com.dialogdialog.bbz.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jiujie.glide.GlideApp;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import x.com.dialogdialog.R;
import x.com.hl_log.ViseLog;
import x.com.hl_rx.bean.UserGoldsBean;
import x.com.hl_tools.hl_screen;
import x.com.hl_tools.hl_time;
import x.com.hl_tools.hl_toast;

/**
 * copyright：
 * author：anthui
 * Version：1.0
 * creation date： 2019/10/17.
 * describe：首页气泡
 */
public class HomeGoldView extends LinearLayout {

//    private int STEP_TYPE=1;

    private static String TAG = "CCJ_TAG";


    public HomeGoldView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.layout_gold, this);

        initView(context);
        ininCLick();
    }


    AnimatorSet set;

    /**
     * 属性集合 将上述动画集合起来一起放一遍
     */
    public void startAnim(View targetView) {


        closeTime();

        if (set != null) {
            set.removeAllListeners();
            set = null;
        }

        set = new AnimatorSet();
        int[] result = getResult(this, targetView);

        set.playTogether(
                ObjectAnimator.ofFloat(this, "translationX", result[0]),
                ObjectAnimator.ofFloat(this, "translationY", result[1]),
                ObjectAnimator.ofFloat(this, "scaleX", 1, 0f),
                ObjectAnimator.ofFloat(this, "scaleY", 1, 0f),
                ObjectAnimator.ofFloat(this, "alpha", 1, 0.0f)
//                ObjectAnimator.ofFloat(translateView, "rotation", 0, 360),
        );

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
//                LogUtil.e("msg===================  hahaha");
//                setVisibility(false);
                setTranslationX(0);
                setTranslationY(0);
                setScaleX(1);
                setScaleY(1);
                setAlpha(1);
            }
        });
        set.setDuration(200);
        set.start();
    }

    private int[] getResult(View transView, View targetView) {
        int[] result = new int[2];
        try {
            int[] transLocation = getLocation(transView);
            int[] targetLocation = getLocation(targetView);
            int transWidth = transView.getWidth();
            int transHeight = transView.getHeight();

            result[0] = (targetLocation[0] + targetView.getWidth() / 2) - (transLocation[0] + transWidth / 2);
            result[1] = (targetLocation[1] + targetView.getHeight() / 2) - (transLocation[1] + transHeight / 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private int[] getLocation(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
        return location;
    }

    Vibrator vibrator;

    private void ininCLick() {
        //默认不显示
        setVisibility(false);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userGold == null) {
                    hl_toast.show(getContext(), "空数据");
//                    setVisibility(false);
                    return;
                }

                if (showInfoDialog()) {
                    return;
                }

                if (onClickCallBack != null) {
                    onClickCallBack.onclickCall(HomeGoldView.this, userGold);
                }


            }
        });

    }

    TextView tvGoldNum;
    TextView tvGoldName;
    //    TextView tvTaskGoldNum;
    TextView tvTime;

    ImageView ivGold;

//    TextView setTexView;
//    TextView setpName;


    private void initView(Context mContext) {
        setOrientation(LinearLayout.VERTICAL);


        tvGoldNum = findViewById(R.id.tv_num);
        tvGoldName = findViewById(R.id.tv_gold_name);
//        tvTaskGoldNum = findViewById(R.id.tv_task_num);
        tvTime = findViewById(R.id.photoTimeText);
        ivGold = findViewById(R.id.iv_gold);

    }

    ValueAnimator animator;

    /**
     * 初始化动画
     *
     * @param
     */
    public void initAnimation(int position) {

        switch (position) {
            case 4:
                animator = ValueAnimator.ofFloat(20, 0, -20, 0, 20);
                break;
            case 1:
                animator = ValueAnimator.ofFloat(0, -20, 0, 20, 0);

                break;
            case 2:
                animator = ValueAnimator.ofFloat(0, 20, 0, -20, 0);

                break;
            case 3:
                animator = ValueAnimator.ofFloat(-20, 0, 20, 0, -20);
                break;
            default:
        }

        //多余操作
        if (animator == null) {
            animator = ValueAnimator.ofFloat(0, 20, 0, -20, 0);
        }
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(2000);
        animator.setRepeatCount(-1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setTranslationY((Float) animation.getAnimatedValue());
            }
        });


    }


    /**
     * 重写隐藏事件，不能直接做  INVISIBLE时需要 禁用事件
     */
    public void setVisibility(boolean visibility) {
        setEnabled(visibility);

        if (visibility) {

            setVisibility(View.VISIBLE);

            if (animator == null) {
                return;
            }
            if (animator.isRunning()) {

            } else {
                animator.start();
            }
        } else {
            closeTime();
            tvTime.setVisibility(GONE);

            setVisibility(View.INVISIBLE);
            if (animator == null) {
                return;
            }
            if (animator.isRunning()) {
                animator.cancel();
            } else {
            }

        }
    }


    UserGoldsBean userGold;
    String text = "";


    /**
     * 根据是否可点击状态、是否可以 默认图 判断 加载图片
     */
    private void loadImage(UserGoldsBean userGold, boolean canClick) {
        int resDefaultImg;
        String imgUrl;

        resDefaultImg = R.mipmap.bg_gold_defaul;
        imgUrl = userGold.getImgUrl();


        text = "";


        if (userGold.getType() == 7) {

            if (userGold.getShowGoldTag() == 1) {
                text = userGold.getGold() + "";
            } else {
                text = "?";
            }

        }


        tvGoldNum.setText(text);

        GlideApp.with(this).load(imgUrl).into(new SimpleTarget<Drawable>() {
            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);

                ivGold.setImageResource(resDefaultImg);
            }

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                ivGold.setImageDrawable(resource);

            }
        });


        //是否为默认图片  默认图 金币放中间，自定义 放右上角
//        if (userGold.getIsDefault()) {
//            GlideApp.with(this).load(imgUrl).error(resDefaultImg).into(ivGold);
//            if (userGold.getType() == 3) {
//                tvGoldNum.setText("?");
//            } else {
//                tvGoldNum.setText(text);
//            }
//        } else {
//
//            GlideApp.with(this).load(imgUrl).into(new SimpleTarget<Drawable>() {
//                @Override
//                public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                    super.onLoadFailed(errorDrawable);
//
//                    ivGold.setImageResource(resDefaultImg);
//
//                    if (userGold.getType() == 3) {
//                        tvGoldNum.setText("?");
//                    } else {
//                        tvGoldNum.setText(text);
//                    }
//
//                }
//
//                @Override
//                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//
//                    ivGold.setImageDrawable(resource);
//                    tvGoldNum.setText("");
//                    if (userGold.getType() == 3) {
//                        tvTaskGoldNum.setText("?");
//                    } else {
//                        tvTaskGoldNum.setText(text);
//
//                    }
//                }
//            });
//        }
    }


    private void setBTextColor() {

        if (btnTextColor != 0) {
            tvGoldName.setTextColor(btnTextColor);
        }
        if (goldTextColor != 0) {
            tvGoldNum.setTextColor(goldTextColor);
        }

        if (timeColor != 0) {
            tvTime.setTextColor(timeColor);
        }
        if (timeBgRes != null) {
            tvTime.setBackground(timeBgRes);
        }

    }


    //type==4时，有数字 1 一号幸运气泡 2二号幸运气泡 3问号幸运气泡 4步数气泡 5任务气泡 6链接气泡
    public void setUserGold(UserGoldsBean userGold) {

        //设置颜色
        setBTextColor();
        if (userGold == null) {
//            LogUtil.e("msg=================回调的数据==========     null");

            setVisibility(false);
            return;
        }
//        LogUtil.e("msg===2==============当前的数据==========     " + JsonUtil.toJson(this.userGold));


//        LogUtil.e("msg===2=======     " + userGold.getType() + "  goid== " + userGold.getGold());
        ;
//
//        long currentTimeMillis = System.currentTimeMillis();
//        long time = 1000 * 3;

//        userGold.setDelayTime(currentTimeMillis + time * 2);
//        userGold.setCountdownTime(currentTimeMillis + time * 4);
//        userGold.setReplaceTime(currentTimeMillis + time * 8);
//        LogUtil.e("msg===================当前金币  " + userGold.getType() + "  gold==" + userGold.getGold());


//        if (userGold.getType() == 7 && userGold.getGold() == 0) {
//            return;
//        }

        this.userGold = userGold;


//        if (userGold.getGold() < 0) {
//            setVisibility(false);
//            return;
//        }
        if (animator == null) {
            initAnimation(userGold.getType());
        }
        //0未领取，1已经领取

//        userGold.setGold(2000);

        if (interval != null) {
            interval.cancel();
            interval = null;
            strings = null;
        }
        if (userGold.getGold() >= 1000) {
            tvGoldNum.setTextSize(16);
        } else {
            tvGoldNum.setTextSize(18);
        }
        //气泡金币数字
//        String text = userGold.getGold() + "";
//        tvTaskGoldNum.setText("");
        tvGoldNum.setText("");

//        switch (userGold.getType()) {
//            case 4:
//            case 1:
//            case 2:
////                tvGoldNum.setText(text);
//                break;
//            case 3:
//                tvGoldNum.setText("?");
//                break;
//            case 5:
//            case 6:
////                tvGoldNum.setText("");
////                tvTaskGoldNum.setText(text);
//                break;
//        }

        if (userGold.getShow()) {
            tvGoldName.setText(userGold.getName());
            tvGoldName.setVisibility(View.VISIBLE);
        } else {
            tvGoldName.setVisibility(View.GONE);
        }
        loadImage(userGold, true);

        initTime(userGold);
        checkCurrentState();
    }


    /**
     * 设置数据时，初始化 时间
     */
    private void initTime(UserGoldsBean userGold) {
        current = state_default;

        delayTime = userGold.getDelayTime() - userGold.getServerTime();//延迟到时间节点的时间 -服务器当前时间


        //初始化倒计时时间
        if (userGold.getServerTime() > userGold.getDelayTime()) {
            countDownTime = userGold.getCountdownTime() - userGold.getServerTime();//倒计时时间节点的时间 -服务器当前时间
        } else {
            countDownTime = userGold.getCountdownTime() - userGold.getDelayTime();//倒计时时间节点的时间 -服务器当前时间
        }

//        WorksDayData everyDayTaskVo = userGold.getEveryDayTaskVo();

        //检测是否为视频  同步本地倒计时 时间
//        if (everyDayTaskVo != null && everyDayTaskVo.getTaskType().equals("videoTask")) {
//
////            Long lookVideoTime = SPUtil.getLong(getContext(), UserInfoManager.INSTANCE + SPUtil.last_video_time, 0);
//
//
//            //限制的时间 5分钟
//            long min = 5 * 60 * 1000;
//            //时间差  到 秒   去除多余的毫秒数据，达到整数的秒
//            long time = ((System.currentTimeMillis() - lookVideoTime) - (System.currentTimeMillis() - lookVideoTime) % 1000);
//            //时间需要倒计时的时间
//            long aLong = min - time;
//
//
//            //如果 当前时间小于本地需要到倒计时 的时候，就同步本地倒计时时间
//            if (countDownTime < aLong) {
//                countDownTime = aLong;
//            }
//        }


        if (userGold.getServerTime() > userGold.getCountdownTime()) {

            replaceTime = userGold.getReplaceTime() - userGold.getServerTime();
        } else {

            replaceTime = userGold.getReplaceTime() - userGold.getCountdownTime();
        }


//        LogUtil.e("服务器时间 " +
//                "" + DateUtil.formatter(DateUtil.Format.SECOND, userGold.getServerTime()) +
//                "  " + DateUtil.formatter(DateUtil.Format.SECOND, userGold.getDelayTime()) +
//                "  " + DateUtil.formatter(DateUtil.Format.SECOND, userGold.getCountdownTime()) +
//                "  " + DateUtil.formatter(DateUtil.Format.SECOND, userGold.getReplaceTime()) +
//
//                "   延迟时间  " + delayTime + "  倒计时时间  " + countDownTime + "  替换时间  " + replaceTime);


    }

    private long delayTime = 0;//延迟时间
    private long countDownTime = 0;//延迟时间
    private long replaceTime = 0;//延迟时间

    public void setOnClickCallBack(OnClickCallBack onClickCallBack) {
        this.onClickCallBack = onClickCallBack;
    }

    OnClickCallBack onClickCallBack;

    public void setTimeColor(int tvNumColor) {

        tvTime.setTextColor(tvNumColor);
    }


    int btnTextColor = 0;
    int goldTextColor = 0;

    int timeColor = 0;
    Drawable timeBgRes;


    /**
     * 设置底文字颜色 动态配置
     */
    public void setTextColor(int btnTextColor, int goldTextColor, String timeColor, String timeBgColor) {

        this.btnTextColor = btnTextColor;
        this.goldTextColor = goldTextColor;


        this.timeColor = Color.parseColor(timeColor);
//        if (AndroidUtils.parseColor(timeColor) != -1) {
//        } else {
//            this.timeColor = Color.WHITE;
//        }


        int color = Color.parseColor(timeBgColor);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(hl_screen.dp2px(getContext(), 7));
        gradientDrawable.setColor(color);

        timeBgRes = gradientDrawable;
//
//
//        if (AndroidUtils.parseColor(timeBgColor) != -1) {
//
//
//
//
//        } else {
//
//            timeBgRes = getContext().getResources().getDrawable(R.drawable.bg_gold_time);
//        }

    }


    public interface OnClickCallBack {
        public void onclickCall(HomeGoldView view, UserGoldsBean userGold);

        void onTimeFinish(HomeGoldView homeGoldView, UserGoldsBean userGold);

        void showInfoDialog(String[] time);
    }

    CountDownTimer interval;
    String[] strings;


//    private void setTimeDown(long time) {
//
//        if (interval != null) {
//            interval.cancel();
//        }
//
//
//        tvTime.setVisibility(View.VISIBLE);
//        interval = RxTimerUtil.interval(time, new RxTimerUtil.IRxNext() {
//            @Override
//            public void doNext(long number) {
////                LogUtil.e("===========================" + number);
//                if (number == 0) {
//                    LogUtil.e("==========================================触发回调");
//                    if (interval != null) {
//                        interval.cancel();
//                    }
//                    tvTime.setVisibility(GONE);
//                    strings = null;
//                    if (onClickCallBack != null) {
//                        onClickCallBack.onTimeFinish();
//                    }
//                } else if (number > 0) {
////                    tvTime.setVisibility(View.VISIBLE);
//
//                    strings = DateUtil.formatHomeTime(number);
//                    String text = strings[0] + ":" + strings[1];
//                    tvTime.setText(text);
//                    tvTime.setVisibility(View.VISIBLE);
//                } else {
//                    tvTime.setVisibility(View.GONE);
//                }
//            }
//        });
//
//
//    }


    private boolean showInfoDialog() {

        if (strings != null) {


            if (onClickCallBack != null) {
                onClickCallBack.showInfoDialog(strings);
                return true;
            }
        }
        return false;


    }


    Disposable subscribe;


    //当前状态， 0默认，1延迟，2倒计时 3 替换
    int current = 0;
    private final int state_default = 0, state_delay = 1, sate_count_down = 2, sate_replace = 3;


    private void checkCurrentState() {

        switch (current) {
            //默认状态
            case state_default:
//
//                LogUtil.e("服务器时间 " +
//                        "" + DateUtil.formatter(DateUtil.Format.SECOND, userGold.getServerTime()) + "   延迟时间  " + delayTime);
                //延迟时间 大于1秒的时候 做倒计时显示
//                long delayShowTime = userGold.getDelayTime() - System.currentTimeMillis();


                if (delayTime > 0) {
                    setVisibility(false);
                    //开始进度倒计时阶段
                    current = state_delay;
                    starDownTime(delayTime);

                    break;
                } else {
                    //直接进入显示 阶段

                }

            case state_delay:

//                long countDown = userGold.getCountdownTime() - System.currentTimeMillis();


                //进入倒计时阶段
                if (countDownTime > 0) {
                    current = sate_count_down;

                    setBtnTime(countDownTime / 1000);

                    tvTime.setVisibility(VISIBLE);
                    setVisibility(true);
//                    setEnabled(false);
                    starDownTime(countDownTime);

                    break;
                } else {

                }


            case sate_count_down:
                tvTime.setVisibility(GONE);
                strings = null;

                setVisibility(true);

                if (replaceTime > 0) {
                    //进入倒计时阶段
                    current = sate_replace;
                    starDownTime(replaceTime);

                    break;
                } else {
                    current = -1;


                    if (userGold.getCountdownTime() == userGold.getReplaceTime()) {
                        return;
                    }

                    if (userGold.getServerTime() > userGold.getReplaceTime()) {
                        if (onClickCallBack != null) {
                            ViseLog.e("===========onTimeFinish=========================1 " + userGold.getServerTime() + "   rel=== " + userGold.getReplaceTime());
                            onClickCallBack.onTimeFinish(this, userGold);
                        }
                    }


                }
                break;
            case sate_replace:

                current = -1;
                if (onClickCallBack != null) {
                    ViseLog.e("===========onTimeFinish=========================2");

                    onClickCallBack.onTimeFinish(this, userGold);
                }

                break;

        }


    }


    /**
     * 显示底部按钮时间
     */
    private void setBtnTime(long time) {


        if (current == sate_count_down) {


            if (time > 0) {
                strings = hl_time.formatHomeTime(time);
                String text = strings[0] + ":" + strings[1];
                tvTime.setText(text);
                tvTime.setVisibility(View.VISIBLE);
            } else {
                strings = null;
                tvTime.setVisibility(View.GONE);
            }

        }

    }

    public void closeTime() {
        if (subscribe != null) {
            if (!subscribe.isDisposed()) {
                subscribe.dispose();
            }
        }
    }

    /**
     * 开始倒计时
     */
    private void starDownTime(long time) {
        closeTime();
        long TOTAL_TIME = time / 1000;

        if (TOTAL_TIME < 1) {
            checkCurrentState();
            return;
        }

        subscribe = Observable.interval(1, 1, TimeUnit.SECONDS)
                .take(TOTAL_TIME)
                .map(aLong -> TOTAL_TIME - aLong - 1)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                }).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {


                        setBtnTime(aLong);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        checkCurrentState();
                    }
                });


    }


}
