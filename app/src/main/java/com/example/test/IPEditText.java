package com.example.test;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


// 这各类用于检测IP地址是否合法，封装了对editext的操作，调用这个类可以返回合法ip值，但是并没有没引入程序中
public class IPEditText extends LinearLayout {
    //控件
    private EditText Edit1;
    private EditText Edit2;
    private EditText Edit3;
    private EditText Edit4;
    //文本
    private String text;
    private String text1;
    private String text2;
    private String text3;
    private String text4;
//
    public IPEditText(final Context context, AttributeSet attrs) {
        super(context,attrs);
        //初始化界面
        Log.d("third","testa");
        View view =     LayoutInflater.from(context).inflate(R.layout.iptext, this);
        //绑定
        Log.d("third","testab");
        Edit1=(EditText)findViewById(R.id.edit1);
        Edit2=(EditText)findViewById(R.id.edit2);
        Edit3=(EditText)findViewById(R.id.edit3);
        Edit4=(EditText)findViewById(R.id.edit4);
        //初始化函数
        init(context);
    }

    private void init(final Context context) {
        /**
         * 监听文本，得到ip段，自动进入下一个输入框
         */
        Edit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text1 = s.toString().trim();
                if (s.length() > 2 ) {
                    if (Integer.parseInt(text1) > 255) {
                        Toast.makeText(context, "请输入合法的ip地址",Toast.LENGTH_LONG).show();
                    }else{
                        Edit2.setFocusable(true);
                        Edit2.requestFocus();
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Edit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text2 = s.toString().trim();
                if (s.length()> 2) {
                    if (Integer.parseInt(text2) > 255) {
                        Toast.makeText(context, "请输入合法的ip地址",Toast.LENGTH_LONG).show();
                    }else{
                        Edit3.setFocusable(true);
                        Edit3.requestFocus();
                    }
                }
                /**
                 * 输入框为空，删除按键则返回上一个文本框
                 */
                /*
                if (start == 0 && s.length() == 0) {
                    Edit1.setFocusable(true);
                    Edit1.requestFocus();
                }
                */
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Edit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text3 = s.toString().trim();
                if (s.length()> 2 ) {

                    if (Integer.parseInt(text3) > 255) {
                        Toast.makeText(context, "请输入合法的ip地址",Toast.LENGTH_LONG).show();
                    }else{
                        Edit4.setFocusable(true);
                        Edit4.requestFocus();
                    }
                }
                /**
                 * 当用户需要删除时,此时的EditText为空时,上一个EditText获得焦点
                 */
                /*
                if (start == 0 && s.length() == 0) {
                    Edit2.setFocusable(true);
                    Edit2.requestFocus();
                }
                */
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Edit4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text4 = s.toString().trim();
                if (s.length() > 2 ) {

                    if (Integer.parseInt(text4) > 255) {
                        Toast.makeText(context, "请输入合法的ip地址",Toast.LENGTH_LONG).show();
                    }
                }
                /**
                 * 当用户需要删除时,此时的EditText为空时,上一个EditText获得焦点
                 */
                /*
                if (start == 0 && s.length() == 0) {
                    Edit3.setFocusable(true);
                    Edit3.requestFocus();
                }
                */
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /**
         *  监听控件，空值时del键返回上一输入框
         */
        Edit2.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(text2==null||text2.isEmpty()){
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        Edit1.setFocusable(true);
                        Edit1.requestFocus();
                    }
                }
                return false;
            }
        });
        Edit3.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(text3==null||text3.isEmpty()){
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        Edit2.setFocusable(true);
                        Edit2.requestFocus();
                    }
                }
                return false;
            }
        });
        Edit4.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(text4==null||text4.isEmpty()){
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        Edit3.setFocusable(true);
                        Edit3.requestFocus();
                    }
                }
                return false;
            }
        });
    }

    /**
     *
     * 成员函数，返回整个ip地址
     */
    public String getText(){
        if (TextUtils.isEmpty(text1) || TextUtils.isEmpty(text2)
                || TextUtils.isEmpty(text3) || TextUtils.isEmpty(text4)) {
            text=null;
        }else {
            text= text1 + "." + text2 + "." + text3 + "." + text4;
        }
        return text;
    }

}
