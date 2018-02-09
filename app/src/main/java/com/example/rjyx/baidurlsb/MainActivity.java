package com.example.rjyx.baidurlsb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.aip.face.AipFace;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.rjyx.baidurlsb.bean.AccessToken;
import com.example.rjyx.baidurlsb.bean.JsonRootBean;
import com.example.rjyx.baidurlsb.http.MyApiService;
import com.example.rjyx.baidurlsb.http.RetrofitUtil;
import com.example.rjyx.baidurlsb.util.Base64Util;
import com.example.rjyx.baidurlsb.util.FileUtil;
import com.example.rjyx.baidurlsb.util.GsonUtils;
import com.example.rjyx.baidurlsb.util.HttpUtil;
import com.google.gson.JsonElement;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Context mContext;
    private List<LocalMedia> selectList = new ArrayList<>();
    TextView textView,num;
    ImageView im_1,im_2;
    //设置APPID/AK/SK
    public static final String APP_ID = "xxxxxxxx";
    public static final String API_KEY = "xxxxxxxxxxxx";
    public static final String SECRET_KEY = "xxxxxxxxxxxxxx";
    AccessToken accessToken = new AccessToken();
    JsonRootBean jsonRootBean = new JsonRootBean();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rlsb_main);
        mContext = this;
        init();

    }

    private void init() {
        textView = (TextView) findViewById(R.id.textView);
        im_1 = (ImageView) findViewById(R.id.im_1);
        im_2 = (ImageView) findViewById(R.id.im_2);
        num = (TextView) findViewById(R.id.num);
    }

    private void getjiance(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 初始化一个AipFace
                AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);

                // 可选：设置网络连接参数
                client.setConnectionTimeoutInMillis(2000);
                client.setSocketTimeoutInMillis(60000);

                // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

                // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
                // 也可以直接通过jvm启动参数设置此环境变量
                System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

                String path = "";
                // 调用接口
                if (getSDPath()!=null) {
                    path = Environment.getExternalStorageDirectory().getPath()+ "/"+"baidu/"+"test.jpg";
                }

                System.out.println("zhao----path---"+path);

                File file = new File(path);
                if (file.exists()){
                    try {
                        JSONObject res = client.detect(path, new HashMap<String, String>());
                        System.out.println("000000000000000.0---"+res.toString(2));
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("zhao----e---"+e.toString());

                    }
                }
            }
        }).start();
    }

    public String getSDPath() {
        File sdDir = null;
        //判断sd卡是否存在
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取根目录
            Log.e("qq", "外部存储可用..." + sdDir.toString());
        }
        return sdDir.toString();
    }

    public void getAccessToken(View view){
        MyApiService myTestApiService = RetrofitUtil.grtRetrofitToGson("https://aip.baidubce.com/");
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("grant_type", "client_credentials");
            requestData.put("client_id", "Iw10ax6Fjb53NPeo1IGo9BPm");
            requestData.put("client_secret", "6xnIUtdEKAzex1ykVKMytlRNput8Grc3");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        Call<JsonElement> doubanCall = myTestApiService.getAccessToken("client_credentials","Iw10ax6Fjb53NPeo1IGo9BPm","6xnIUtdEKAzex1ykVKMytlRNput8Grc3");

        doubanCall.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                System.out.println("zhao-----getAccessToken---isSuccessful--------"+response);
                textView.setText("成功。。getAccessToken。"+response.body().toString());
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        System.out.println("zhao-----getAccessToken---access_token--------"+jsonObject.optString("access_token"));
                        accessToken.access_token = jsonObject.optString("access_token");
                        accessToken.session_key = jsonObject.optString("session_key");
                        accessToken.refresh_token = jsonObject.optString("refresh_token");
                        accessToken.scope = jsonObject.optString("scope");
                        accessToken.expires_in = jsonObject.optString("expires_in");
                        accessToken.session_secret = jsonObject.optString("session_secret");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                System.out.println("zhao-----getAccessToken-----onFailure------");
                textView.setText("失败。。getAccessToken。"+call.toString());

            }
        });
    }




    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    jsonRootBean = GsonUtils.fromJson(msg.obj.toString(),JsonRootBean.class);
                    if (jsonRootBean.getResult()!=null && jsonRootBean.getResult().size()>0){
                        num.setText(String.valueOf(jsonRootBean.getResult().get(0).getScore()));
                    }else {
                        num.setText("匹配失败");
                    }
                    break;
                case 2:
                    num.setText("匹配失败");
                    break;

                default:
                    break;
            }

        }
    };


    public void addPic(View view) {
        selectList.clear();
        num.setText("");
        PictureSelector.create(MainActivity.this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .theme(R.style.picture_Sina_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(2)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(false)// 是否可预览图片 true or false
                .previewVideo(true)// 是否可预览视频 true or false
                .enablePreviewAudio(false) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(1f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/PatientPath")// 自定义拍照保存路径,可不填
                .enableCrop(false)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
//                    .glideOverride(160, 160)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                    .withAspectRatio()// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(true)// 是否显示gif图片 true or false
//                    .compressSavePath(getPath())//压缩图片保存地址
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .selectionMedia(selectList)// 是否传入已选图片 List<LocalMedia> list
//                    .previewEggs()// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
//                    .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(100)// 小于100kb的图片不压缩
//                    .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                    .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
//                    .rotateEnabled() // 裁剪是否可旋转图片 true or false
//                    .scaleEnabled()// 裁剪是否可放大缩小图片 true or false
//                    .videoQuality()// 视频录制质量 0 or 1 int
//                    .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
//                    .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
//                    .recordVideoSecond()//视频秒数录制 默认60s int
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList != null && selectList.size() > 0) {
                        RequestOptions options = new RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.deafault)
                                .error(R.drawable.load_failed)
                                .diskCacheStrategy(DiskCacheStrategy.ALL);

                        Glide.with(mContext)
                                .load(selectList.get(0).getPath())
                                .apply(options)
                                .into(im_1);

                        Glide.with(mContext)
                                .load(selectList.get(1).getPath())
                                .apply(options)
                                .into(im_2);

                    }

                    break;
                default:
                    break;
            }
        }
    }

    public void getFace(View view){
        num.setText("");
        // 请求url
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "https://aip.baidubce.com/rest/2.0/face/v2/match";
                try {
                    // 本地文件路径
                    String filePath = selectList.get(0).getPath();
                    byte[] imgData = FileUtil.readFileByBytes(filePath);
                    String imgStr = Base64Util.encode(imgData);
                    String imgParam = URLEncoder.encode(imgStr, "UTF-8");

                    String filePath2 = selectList.get(1).getPath();
                    byte[] imgData2 = FileUtil.readFileByBytes(filePath2);
                    String imgStr2 = Base64Util.encode(imgData2);
                    String imgParam2 = URLEncoder.encode(imgStr2, "UTF-8");

                    String param = "images=" + imgParam + "," + imgParam2;

                    // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
                    String access_Token = accessToken.access_token;

                    String result = HttpUtil.post(url, access_Token, param);


                    System.out.println("zhao---result--"+result);

                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = result;
                    handler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

}
