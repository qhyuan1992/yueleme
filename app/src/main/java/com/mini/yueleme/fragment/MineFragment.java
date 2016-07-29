package com.mini.yueleme.fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.mini.yueleme.R;
import com.mini.yueleme.SettingActivity;
import com.mini.yueleme.YLMApplication;
import com.mini.yueleme.utils.Constant;
import com.mini.yueleme.utils.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/*我的用于展示个人中心*/

public class MineFragment extends Fragment implements View.OnClickListener{

	private SVProgressHUD syncDialog;
	private YLMApplication application;
	private View mineView;
	private RelativeLayout settingsLayout;
	private EditText personalizedSignatureEdit, ageEdit, telNumEdit;
	private TextView personalizedSignatureText, departmentText, emotionText, ageText, telNumText, sexText;
	private Spinner spinnerDepartment, spinnerEmotion, spinnerSex;
	private CircleImageView userImageIv;

	private ImageView headPortraitBG;
	private TextView nicknameText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		mineView = inflater.inflate(R.layout.personal_center, null);
		syncDialog = new SVProgressHUD(getActivity());
		initView(mineView);
		return mineView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		application = (YLMApplication) getActivity().getApplication();

		ImageLoader.getInstance().displayImage(application.userImageUrl,userImageIv);
		ImageLoader.getInstance().displayImage(application.userImageUrl,headPortraitBG);
		ImageUtil.setBurryImageSrc(headPortraitBG, 10);
		nicknameText.setText(application.userName);


		settingsLayout.setOnClickListener(this);
		sexText.setText(application.prefUtils.getString("sex"));
		personalizedSignatureText.setText(application.prefUtils.getString("sign"));
		telNumText.setText(application.prefUtils.getString("tel"));
		departmentText.setText(application.prefUtils.getString("group"));
		ageText.setText(application.prefUtils.getString("age"));
		emotionText.setText(application.prefUtils.getString("single"));
	}


	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.settings:
				startActivity(new Intent(getActivity(), SettingActivity.class));
				break;
			default:
				break;
		}
	}

	public void startEditMode() {
		String personalizedSignature, age, telNum;
		personalizedSignature = personalizedSignatureText.getText().toString();
		age = ageText.getText().toString();
		telNum = telNumText.getText().toString();
		personalizedSignatureText.setVisibility(View.GONE);
		departmentText.setVisibility(View.GONE);
		emotionText.setVisibility(View.GONE);
		ageText.setVisibility(View.GONE);
		telNumText.setVisibility(View.GONE);
		sexText.setVisibility(View.GONE);

		personalizedSignatureEdit.setVisibility(View.VISIBLE);
		spinnerDepartment.setVisibility(View.VISIBLE);
		spinnerSex.setVisibility(View.VISIBLE);
		spinnerEmotion.setVisibility(View.VISIBLE);
		ageEdit.setVisibility(View.VISIBLE);
		telNumEdit.setVisibility(View.VISIBLE);

		personalizedSignatureEdit.setText(personalizedSignature);
		ageEdit.setText(age);
		telNumEdit.setText(telNum);

	}

	public boolean saveData() {
		String personalizedSignature, age, telNum;

		personalizedSignature = personalizedSignatureEdit.getText().toString();
		age = ageEdit.getText().toString();
		telNum = telNumEdit.getText().toString();
		if (telNum.equals("")) {
			application.showToastMsg("电话号码不能为空");
			return false;
		}
		if (age.equals("")) {
			application.showToastMsg("年龄不能为空");
			return false;
		}
		if (personalizedSignature.length() > 20) {
			Toast.makeText(getActivity(), "个性签名长度限制20个字", Toast.LENGTH_SHORT).show();
			return false;
		}

		personalizedSignatureText.setVisibility(View.VISIBLE);
		departmentText.setVisibility(View.VISIBLE);
		emotionText.setVisibility(View.VISIBLE);
		ageText.setVisibility(View.VISIBLE);
		telNumText.setVisibility(View.VISIBLE);
		sexText.setVisibility(View.VISIBLE);


		personalizedSignatureEdit.setVisibility(View.GONE);
		spinnerDepartment.setVisibility(View.GONE);
		spinnerEmotion.setVisibility(View.GONE);
		spinnerSex.setVisibility(View.GONE);
		ageEdit.setVisibility(View.GONE);
		telNumEdit.setVisibility(View.GONE);

		personalizedSignatureText.setText(personalizedSignature);
		ageText.setText(age);
		telNumText.setText(telNum);

		/*这里默认用户每次点击编辑按钮都会对数据进行更改, 所以每次保存时我们都会对服务器进行post请求*/
		/*上面只是在本地进行了操作,还应该把编辑的信息同步到服务器中, 之后再实现*/

		netSync(personalizedSignature, emotionText.getText().toString(), age,
				departmentText.getText().toString(), sexText.getText().toString(), telNum);

		return true;

	}

	public void initView(View view) {
		userImageIv = (CircleImageView)mineView.findViewById(R.id.headPortraitImage);

		headPortraitBG = (ImageView) view.findViewById(R.id.headPortraitBG);

		nicknameText = (TextView) view.findViewById(R.id.nicknameText);

		settingsLayout = (RelativeLayout)view.findViewById(R.id.settings);
		personalizedSignatureEdit = (EditText)view.findViewById(R.id.personalizedSignatureEdit);
		ageEdit = (EditText)view.findViewById(R.id.ageEdit);
		telNumEdit = (EditText)view.findViewById(R.id.telNumEdit);

		personalizedSignatureText = (TextView)view.findViewById(R.id.personalizedSignatureContent);
		departmentText = (TextView)view.findViewById(R.id.departmentContent);
		emotionText = (TextView)view.findViewById(R.id.emotionContent);
		ageText = (TextView)view.findViewById(R.id.ageContent);
		telNumText = (TextView)view.findViewById(R.id.telNumContent);
		sexText = (TextView)view.findViewById(R.id.sexContent);

		spinnerDepartment = (Spinner)view.findViewById(R.id.spinner_department);
		spinnerEmotion = (Spinner)view.findViewById(R.id.spinner_emotion);
		spinnerSex = (Spinner)view.findViewById(R.id.spinner_sex);

		spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				departmentText.setText(getResources().getStringArray(R.array.department)[i]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

		spinnerEmotion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				emotionText.setText(getResources().getStringArray(R.array.single)[i]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

		spinnerSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				sexText.setText(getResources().getStringArray(R.array.sex)[i]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});
	}

	/*用户更改信息后的网络同步*/
	public void netSync(final String personalizedSignature, final String emotion, final String age, final String department, final String sex, final String telNum) {
		syncDialog.showWithStatus("提交中...");
		JSONObject infoParams = new JSONObject();
		try {
			infoParams.put("user_name", "bumblebee");
			infoParams.put("user_id", "88F88C99644461F523E562179444C039");
			infoParams.put("group", department);
			infoParams.put("sex", sex.equals("男") ? 0 : 1);
			infoParams.put("age", Integer.valueOf(age));
			infoParams.put("tel", telNum);
			infoParams.put("sign", personalizedSignature);
			infoParams.put("single", emotion.equals("单身") ? 1 : 0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JsonObjectRequest infoJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constant.INFO_SYNC, infoParams,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {
						int code = -1;
						try {
							code = jsonObject.getInt("code");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if(0 == code) {
							syncDialog.showSuccessWithStatus("提交成功");
							// 更新本地存储的用户信息
							application.prefUtils.restoreString("sex",sex.equals("男") ? "男":"女");
							application.prefUtils.restoreString("group",department);
							application.prefUtils.restoreString("age",age);
							application.prefUtils.restoreString("tel",telNum);
							application.prefUtils.restoreString("sign",personalizedSignature);
							application.prefUtils.restoreString("single",emotion.equals("单身") ? "单身":"非单身");
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				application.showToastMsg("网络错误，上传信息失败");
				syncDialog.showSuccessWithStatus("提交失败");
			}
		});
		application.requestQueue.add(infoJsonObjectRequest);
	}
}
