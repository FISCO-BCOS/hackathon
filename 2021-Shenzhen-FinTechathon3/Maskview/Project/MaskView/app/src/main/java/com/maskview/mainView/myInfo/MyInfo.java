package com.maskview.mainView.myInfo;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.sl.utakephoto.compress.CompressConfig;
import com.sl.utakephoto.crop.CropOptions;
import com.sl.utakephoto.exception.TakeException;
import com.sl.utakephoto.manager.ITakePhotoResult;
import com.sl.utakephoto.manager.UTakePhoto;
import com.maskview.R;
import com.maskview.dao.UserRequest;
import com.maskview.util.UtilParameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyInfo extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private AlertDialog alertDialog;
    private TextView tv_myNickName;
    private TextView tv_sex;
    private TextView tv_birth;
    private TextView tv_phoneNumber;
    private ImageView mIvHeadView;
    private static int RESULT_LOAD_IMAGE = 1;  //成功选取照片后的返回值
    private UserRequest mRequest;
    private String nickName;
    private String headViewPath;
    private String sex;
    private String birth;
    private File privateFile;
    private static final int RESULT_SUCCESS = 0;
    private static final int RESULT_FAIL = 1;
    private static final int NO_RESPONSE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        initView();
        getMyInfo();
    }

    private void initView() {
        mContext = this;
        mRequest = new UserRequest();
        ImageView iv_finishThisActivity = findViewById(R.id.myInfo_finishThisActivity);
        iv_finishThisActivity.setOnClickListener(this);
        LinearLayout layout_setNickName = findViewById(R.id.layout_myInfo_nickName);
        layout_setNickName.setOnClickListener(this);
        LinearLayout layout_setSex = findViewById(R.id.layout_myInfo_sex);
        layout_setSex.setOnClickListener(this);
        LinearLayout layout_setBirth = findViewById(R.id.layout_myInfo_birth);
        layout_setBirth.setOnClickListener(this);
        LinearLayout layout_setHeadView = findViewById(R.id.layout_myInfo_headView);
        layout_setHeadView.setOnClickListener(this);
        tv_myNickName = findViewById(R.id.tv_myInfo_nickName);
        tv_sex = findViewById(R.id.tv_myInfo_sex);
        tv_birth = findViewById(R.id.tv_myInfo_birth);
        tv_phoneNumber = findViewById(R.id.tv_myInfo_phoneNumber);
        mIvHeadView = findViewById(R.id.iv_myInfo_headView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myInfo_finishThisActivity:
                finish();
                overridePendingTransition(0, R.anim.out_to_right);
                break;
            case R.id.layout_myInfo_nickName:
                setNickName();
                break;
            case R.id.layout_myInfo_sex:
                setSex();
                break;
            case R.id.layout_myInfo_birth:
                setBirth();
                break;
            case R.id.layout_myInfo_headView:
                updateMyHeadView();
                /*Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, RESULT_LOAD_IMAGE);*/
                break;
        }
    }

    private void updateMyHeadView() {
        UTakePhoto.with(MyInfo.this)
                .openAlbum()
                .setCrop(new CropOptions.Builder()
                        .setAspectX(1)
                        .setAspectY(1)
                        .setOutputX(10)
                        .setOutputY(10)
                        .setWithOwnCrop(true)
                        .create())
                .setCompressConfig(new CompressConfig.Builder().create())
                .build(new ITakePhotoResult() {
                    @Override
                    public void takeSuccess(List<Uri> uriList) {
                        // 裁剪成功后, 上传私有目录图片, 上传成功后删除
                        final Uri uri = uriList.get(0);
                        // 此处的uri就是私有目录的uri
                        String privatePath = uri.getPath();
                        assert privatePath != null;
                        privateFile = new File(privatePath);
                        final String imgName = privateFile.getName();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String uploadResult = mRequest.uploadImage(privateFile, UtilParameter.uploadImgUrl, UtilParameter.myToken);
                                //String uploadResult = mRequest.uploadImage(privateFile, "https://10.0.2.2:718/restricted/uploadImg", UtilParameter.myToken);
                                if (!uploadResult.equals("")) {
                                    if (uploadResult.contains("true")) {
                                        // 图片上传成功,上传数据
                                        final String result = mRequest.updateMyHeadView(imgName, UtilParameter.myToken);
                                        if (result.contains("true")) {
                                            /*// 上传图片数据成功,删除私有目录下裁剪的图片
                                            if (privateFile.exists()) {
                                                privateFile.delete();
                                            }*/
                                            myHandler.sendEmptyMessage(RESULT_SUCCESS);
                                        } else {
                                            myHandler.sendEmptyMessage(RESULT_FAIL);
                                        }
                                    } else {
                                        myHandler.sendEmptyMessage(RESULT_FAIL);
                                    }
                                } else {
                                    myHandler.sendEmptyMessage(NO_RESPONSE);
                                }
                            }
                        }).start();

                    }

                    @Override
                    public void takeFailure(TakeException ex) {

                    }

                    @Override
                    public void takeCancel() {
                        finish();
                    }
                });
    }

    private Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == RESULT_SUCCESS) {
                mIvHeadView.setImageURI(Uri.parse(privateFile.toString()));
                if (privateFile.exists()) {
                    boolean a = privateFile.delete();
                    Log.e("", "删除结果: " + a);
                }
                //Toast.makeText(mContext, "成功", Toast.LENGTH_SHORT).show();
            } else if (msg.what == RESULT_FAIL) {
                Toast.makeText(mContext, "失败,请稍后重试", Toast.LENGTH_SHORT).show();
            } else if (msg.what == NO_RESPONSE) {
                Toast.makeText(mContext, "网络不佳", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

    private void getMyInfo() {
        ExecutorService es = Executors.newCachedThreadPool();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                String result = mRequest.getMyInfo(UtilParameter.myToken);
                if (result.contains("true")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        nickName = jsonObject.get("userName") + "";
                        headViewPath = jsonObject.get("headViewPath") + "";
                        sex = jsonObject.get("sex") + "";
                        birth = jsonObject.get("birth") + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        es.submit(task);
        es.shutdown();

        while (true) {
            if (es.isTerminated()) {
                tv_myNickName.setText(nickName);
                String phoneNum = UtilParameter.myPhoneNumber;
                String hiddenPhone = phoneNum.substring(0, 3) + "****" + phoneNum.substring(7);
                tv_phoneNumber.setText(hiddenPhone);
                if (!sex.equals("")) {
                    tv_sex.setText(sex);
                }
                if (!birth.equals("")) {
                    //birth = birth.substring(0, birth.indexOf("T"));
                    tv_birth.setText(birth);
                }
                if (!headViewPath.equals("")) {
                    String headUrl = UtilParameter.IMAGES_IP + headViewPath;
                    Glide.with(mContext).load(headUrl).into(mIvHeadView);
                }
                break;
            }
        }
    }

    private void setNickName() {
        View view = View.inflate(mContext, R.layout.alert_nick_name, null);
        alertDialog = new AlertDialog.Builder(mContext).setView(view).setTitle("昵称")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        EditText et_nickName = alertDialog.findViewById(R.id.alert_et__nickName);
                        if (et_nickName != null) {
                            final String nickName = et_nickName.getText().toString().trim();
                            if (nickName.equals(tv_myNickName.getText().toString().trim())) {
                                dialog.cancel();
                                return;
                            }
                            if (!nickName.equals("")) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        final String result = mRequest.updateMyNickName(nickName, UtilParameter.myToken);
                                        Log.e("--------", "result: " + result);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (!result.equals("")) {
                                                    if (result.contains("true")) {
                                                        dialog.cancel();
                                                        tv_myNickName.setText(nickName + "");
                                                        UtilParameter.myNickName = nickName;
                                                    } else {
                                                        dialog.cancel();
                                                        Toast.makeText(mContext, "昵称不可用", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(mContext, "服务器未响应,请稍后重试", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }).start();

                            } else {
                                tv_myNickName.setText("未设置");
                            }
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    private void setSex() {
        final String[] sex_list = {"男", "女", "保密"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("性别");
        builder.setSingleChoiceItems(sex_list, 2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                final String sex = sex_list[which];
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String result = mRequest.updateMySex(sex, UtilParameter.myToken);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (result.contains("true")) {
                                    dialog.cancel();
                                    tv_sex.setText(sex);
                                }
                            }
                        });
                    }
                }).start();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setBirth() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);    //获取年月日
        int month = cal.get(Calendar.MONTH);  //获取到的月份是从0开始计数
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day) {
                //将选择的日期,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                final String birth = year + "-" + (++month) + "-" + day;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String result = mRequest.updateMyBirth(birth, UtilParameter.myToken);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (result.contains("true")) {
                                    tv_birth.setText(birth);
                                }
                            }
                        });
                    }
                }).start();
            }
        };
        //后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
        DatePickerDialog dialog = new
                DatePickerDialog(MyInfo.this, DatePickerDialog.THEME_HOLO_LIGHT, listener, year, month, day);
        DatePicker datePicker = dialog.getDatePicker();
        //出生日期最大选当天
        datePicker.setMaxDate(new Date().getTime());
        dialog.show();
    }

    //选取完照片后设置头像---未裁剪
    /*protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            final Uri uri = data.getData();
            // Android10及以上
            if (Build.VERSION.SDK_INT >= 29) {
                DocumentFile documentFile = DocumentFile.fromSingleUri(mContext, uri);
                // 所选的共享目录下图片名称---名称带类型:xxx.jpg
                final String imgName = documentFile.getName();
                // 将选中的共享目录的照片复制到私有目录, 并获取的复制后的私有路径
                String privatePath = GetSDPath.getPrivatePath(mContext, uri, imgName);
                // 将私有目录下的照片Path->File,上传
                final File file = new File(privatePath);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //上传图片
                        String uploadImageResult = "";
                        uploadImageResult = mRequest .uploadImageSetName(file, imgName, UtilParameter.uploadImgUrl, UtilParameter.myToken);
                        if (uploadImageResult.contains("true")) {
                            //图片上传成功,上传数据
                            final String result = mRequest.updateMyHeadView(imgName, UtilParameter.myToken);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (result.contains("true")) {
                                        try {
                                            ContentResolver cr = mContext.getContentResolver();
                                            Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                                            iv_headView.setImageBitmap(bitmap);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                    }
                }).start();
            } else {
                // Android10以下
                DocumentFile documentFile = DocumentFile.fromSingleUri(mContext, uri);
                if (documentFile != null) {
                    final String myHeadViewName = documentFile.getName(); //文件名称
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //上传图片
                            String uploadImageResult = "";
                            String imgPath = ImageUriUtil.getPhotoPathFromContentUri(mContext, uri);
                            File file = new File(imgPath);
                            uploadImageResult = mRequest.uploadImage(file, UtilParameter.uploadImgUrl, UtilParameter.myToken);
                            if (uploadImageResult.contains("true")) {
                                //图片上传成功,上传数据
                                final String result = mRequest.updateMyHeadView(myHeadViewName, UtilParameter.myToken);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (result.contains("true")) {
                                            try {
                                                ContentResolver cr = mContext.getContentResolver();
                                                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                                                iv_headView.setImageBitmap(bitmap);
                                            } catch (FileNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        }
    }*/

    // 自带的返回键跳转页面并finish当前页面
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(0, R.anim.out_to_right);
        }
        return super.dispatchKeyEvent(event);
    }
}
