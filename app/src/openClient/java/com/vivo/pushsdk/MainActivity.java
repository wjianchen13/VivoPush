package com.vivo.pushsdk;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ScrollView;

import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;
import com.vivo.push.ups.CodeResult;
import com.vivo.push.ups.TokenResult;
import com.vivo.push.ups.UPSRegisterCallback;
import com.vivo.push.ups.UPSTurnCallback;
import com.vivo.push.ups.VUpsManager;
import com.vivo.push.util.VivoPushException;
import com.vivo.pushsdk.weiget.LogView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements IPushActionListener {
    private static final String TAG = "VivoPush";
    private LogView mLogView;
    private ScrollView mScrollView;
    private static final String APPID = "10000";
    private static final String API_KEY = "9c29fe5f-ea67-46d3-951f-81da78d2c029";
    private static final String APP_SECRET = "7265f2a4-ebbb-44bf-88b9-b03e67dfdc5b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScrollView = findViewById(R.id.scrollView);
        mLogView = findViewById(R.id.log_content);
        View root = findViewById(R.id.root);
        View logFrame = findViewById(R.id.log_frame);
        mLogView.setViews(root, logFrame, mScrollView);
        mLogView.invalidate();
        CheckBox logFilter = findViewById(R.id.log_filter);
        logFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mLogView.filter(isChecked);
            }
        });
        try {
            PushClient.getInstance(MainActivity.this).initialize();
        } catch (VivoPushException e) {
            e.printStackTrace();
        }
        String regId = PushClient.getInstance(MainActivity.this).getRegId();
        Log.d(TAG, " regId= " + regId);
        Button bind = findViewById(R.id.bind);
        Button unbind = findViewById(R.id.unbind);
        Button getRegID = findViewById(R.id.getRegID);
        bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 打开push开关, 关闭为turnOffPush，详见api接入文档
                PushClient.getInstance(getApplicationContext()).turnOnPush(new IPushActionListener() {
                    @Override
                    public void onStateChanged(int state) {
                        // TODO: 开关状态处理， 0代表成功
                        Log.d(TAG, "turnOnPush state= " + state);
                        String regId = "";
                        if (state == 0) {
                            regId = PushClient.getInstance(MainActivity.this).getRegId();
                            Log.d(TAG, " regId= " + regId);
                        }
                        String log1 = "turnOnPush state= " + state + " regId= " + regId;
                        updateDisplay(log1);
                    }
                });
            }
        });
        unbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 关闭push开关, 关闭为turnOffPush，详见api接入文档
                PushClient.getInstance(getApplicationContext()).turnOffPush(new IPushActionListener() {
                    @Override
                    public void onStateChanged(int state) {
                        // TODO: 开关状态处理， 0代表成功
                        Log.d(TAG, " turnOffPush state= " + state);
                        String log = " turnOffPush state= " + state;
                        updateDisplay(log);
                        if (state == 0) {
                            String regId = PushClient.getInstance(MainActivity.this).getRegId();
                            Log.d(TAG, " regId= " + regId);
                            String log1 = " regId= " + regId;
                            updateDisplay(log1);
                        }
                    }
                });
            }
        });
        getRegID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String regId = PushClient.getInstance(MainActivity.this).getRegId();
                Log.d(TAG, " regId= " + regId);
                String log = " regId= " + regId;
                updateDisplay(log);
            }
        });
        Button intent = findViewById(R.id.intent);
        intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getIntentParma();
            }
        });
        Button checkManifest = findViewById(R.id.checkManifest);
        checkManifest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PushClient.getInstance(MainActivity.this).checkManifest();
                } catch (VivoPushException e) {
                    e.printStackTrace();
                }

            }
        });

        Button bindAlias = findViewById(R.id.bindAlias);
        bindAlias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PushClient.getInstance(MainActivity.this).bindAlias(TAG, new IPushActionListener() {
                    @Override
                    public void onStateChanged(int i) {
                        Log.d(TAG, " bindAlias= " + i);
                        String log = " bindAlias= " + i + " alias = " + TAG;
                        updateDisplay(log);
                    }
                });
            }
        });

        Button unBindAlias = findViewById(R.id.unBindAlias);
        unBindAlias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PushClient.getInstance(MainActivity.this).unBindAlias(TAG, new IPushActionListener() {
                    @Override
                    public void onStateChanged(int i) {
                        Log.d(TAG, " unbindAlias= " + i);
                        String log = " unbindAlias= " + i + " alias = " + TAG;
                        updateDisplay(log);
                    }
                });
            }
        });

        Button getAlias = findViewById(R.id.getAlias);
        getAlias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String alias = PushClient.getInstance(MainActivity.this).getAlias();
                Log.d(TAG, " getAlias= " + alias);
                String log = " getAlias= " + alias;
                updateDisplay(log);
            }
        });

        Button setTopic = findViewById(R.id.setTopic);
        setTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PushClient.getInstance(MainActivity.this).setTopic(TAG, new IPushActionListener() {
                    @Override
                    public void onStateChanged(int i) {
                        Log.d(TAG, " setTopic= " + i);
                        String log = " setTopic= " + i + " topic=  " + TAG;
                        updateDisplay(log);
                    }
                });
            }
        });

        Button delTopic = findViewById(R.id.delTopic);
        delTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PushClient.getInstance(MainActivity.this).delTopic(TAG, new IPushActionListener() {
                    @Override
                    public void onStateChanged(int i) {
                        Log.d(TAG, " delTopic= " + i);
                        String log = " delTopic= " + i + " topic=  " + TAG;
                        updateDisplay(log);
                    }
                });
            }
        });

        Button getTopics = findViewById(R.id.getTopics);
        getTopics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> alias = PushClient.getInstance(MainActivity.this).getTopics();
                Log.d(TAG, " getTopics size = " + alias.size() + " getTopics " + alias.toString());
                String log = " getTopics size = " + alias.size() + " getTopics " + alias.toString();
                updateDisplay(log);
            }
        });

        Button isSupport = findViewById(R.id.isSupport);
        isSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean issupport = PushClient.getInstance(MainActivity.this).isSupport();
                Log.d(TAG, " issupport  = " + issupport);
                String log = " issupport  = " + issupport;
                updateDisplay(log);
            }
        });


        Button VUpsRegisterToken = findViewById(R.id.VUpsRegisterToken);
        VUpsRegisterToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VUpsManager.getInstance().registerToken(MainActivity.this, APPID, API_KEY, APP_SECRET, new UPSRegisterCallback() {
                    @Override
                    public void onResult(TokenResult tokenResult) {
                        Log.d(TAG, " issupport  = " + tokenResult.getReturnCode());
                        String log = "";

                        if (tokenResult.getReturnCode() == 0) {
                            Log.d(TAG, "注册成功 regID = " + tokenResult.getToken());
                            log = "注册成功 regID = " + tokenResult.getToken();
                        } else {
                            Log.d(TAG, "注册失败");
                            log = "注册失败";
                        }
                        updateDisplay(log);
                    }
                });


            }
        });

        Button VUpsUnRegisterToken = findViewById(R.id.VUpsUnRegisterToken);
        VUpsUnRegisterToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VUpsManager.getInstance().unRegisterToken(MainActivity.this, new UPSRegisterCallback() {
                    @Override
                    public void onResult(TokenResult tokenResult) {
                        Log.d(TAG, " issupport  = " + tokenResult.getReturnCode());
                        String log = "";

                        if (tokenResult.getReturnCode() == 0) {
                            Log.d(TAG, "解注册成功 regID = " + tokenResult.getToken());
                            log = "解注册成功 regID = " + tokenResult.getToken();
                        } else {
                            Log.d(TAG, "解注册成功");
                            log = "解注册成功";
                        }
                        updateDisplay(log);
                    }
                });
            }
        });

        Button VUpsTurnOnPush = findViewById(R.id.VUpsTurnOnPush);
        VUpsTurnOnPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VUpsManager.getInstance().turnOnPush(MainActivity.this, new UPSTurnCallback() {
                    @Override
                    public void onResult(CodeResult codeResult) {
                        Log.d(TAG, " issupport  = " + codeResult.getReturnCode());
                        String log = "";

                        if (codeResult.getReturnCode() == 0) {
                            Log.d(TAG, "注册成功 regID = " + codeResult.getReturnCode());
                            log = "注册成功 regID = " + codeResult.getReturnCode();
                        } else {
                            Log.d(TAG, "注册失败");
                            log = "注册失败";
                        }
                        updateDisplay(log);
                    }
                });


            }
        });

        Button VUpsTurnOffPush = findViewById(R.id.VUpsTurnOffPush);
        VUpsTurnOffPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VUpsManager.getInstance().turnOffPush(MainActivity.this, new UPSTurnCallback() {
                    @Override
                    public void onResult(CodeResult codeResult) {
                        Log.d(TAG, " issupport  = " + codeResult.getReturnCode());
                        String log = "";

                        if (codeResult.getReturnCode() == 0) {
                            Log.d(TAG, "解订阅成功 regID = " + codeResult.getReturnCode());
                            log = "解订阅成功 regID = " + codeResult.getReturnCode();
                        } else {
                            Log.d(TAG, "解订阅失败");
                            log = "解订阅失败";
                        }
                        updateDisplay(log);
                    }
                });


            }
        });


    }

    private void getIntentParma() {
        //        在Android 开发工具中，参考如下代码生成 Intent
        android.content.Intent intent = new Intent(this, CustomActivity.class);
        //Scheme协议（vpushscheme://com.vivo.push.notifysdk/detail?）开发者可以自定义
        intent.setData(Uri.parse("vpushscheme://com.vivo.pushtest/detail?"));
        //intent 中添加自定义键值对，value 为 String 型
        intent.putExtra("key1", "xxx");
        //intent 中添加自定义键值对，value 为 Integer 型
        intent.putExtra("key2", 2);
        //得到intent url 值
        String intentUri = intent.toUri(Intent.URI_INTENT_SCHEME);


        String log = " intentUri= " + intentUri;
        Log.d(TAG, " intentUri= " + intentUri);
        updateDisplay(log);
    }

    @Override
    public void onStateChanged(int i) {
        Log.d(TAG, " onStateChanged= " + i);
        String regId = PushClient.getInstance(MainActivity.this).getRegId();
        Log.d(TAG, " regId= " + regId);
    }

    public void clearLog(View v) {

        if (mLogView != null) {
            mLogView.clear();
        }
    }

    public void seperatLog(View v) {

        if (mLogView != null) {
            mLogView.seperate();
        }
    }

    public static final int LEVEL_INFO = 0;
    public static final int LEVEL_WARN = LEVEL_INFO + 1;

    public void updateDisplay(final String logMsg) {
        updateDisplay(logMsg, LEVEL_WARN, false);
    }

    // 更新界面显示内容
    public void updateDisplay(final String logMsg, final int logLeve, final boolean serverLog) {

        if (mLogView != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLogView.setText(logMsg, logLeve, serverLog);
                    Log.d(TAG, "" + logMsg);
                }
            });
        }
    }
}
