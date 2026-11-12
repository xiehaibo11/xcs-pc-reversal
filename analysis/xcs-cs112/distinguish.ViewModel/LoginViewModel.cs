using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading;
using System.Windows;
using System.Windows.Threading;
using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Command;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using distinguish.Common;
using distinguish.Model;

namespace distinguish.ViewModel;

public class LoginViewModel : ViewModelBase
{
	private Visibility isVisResetBut;

	private Visibility isVisServer;

	private string appVersion;

	private bool savePwdCheckBox;

	private string userName;

	private Visibility loginButVis;

	private Visibility loginButIngVis = (Visibility)2;

	private Visibility loginLoading = (Visibility)1;

	public string pwd;

	public string ipAddr;

	public bool loginButIsDisable;

	public string loginButContent;

	public string UserName
	{
		get
		{
			return userName;
		}
		set
		{
			userName = value;
			((ObservableObject)this).RaisePropertyChanged("UserName");
		}
	}

	public string Pwd
	{
		get
		{
			return pwd;
		}
		set
		{
			pwd = value;
			((ObservableObject)this).RaisePropertyChanged("Pwd");
		}
	}

	public string IpAddr
	{
		get
		{
			return ipAddr;
		}
		set
		{
			ipAddr = value;
			((ObservableObject)this).RaisePropertyChanged("IpAddr");
		}
	}

	public bool LoginButIsDisable
	{
		get
		{
			return loginButIsDisable;
		}
		set
		{
			loginButIsDisable = value;
			((ObservableObject)this).RaisePropertyChanged("LoginButIsDisable");
		}
	}

	public string LoginButContent
	{
		get
		{
			return loginButContent;
		}
		set
		{
			loginButContent = value;
			((ObservableObject)this).RaisePropertyChanged("LoginButContent");
		}
	}

	public RelayCommand<Window> LoginCommand { get; set; }

	public RelayCommand<Window> ResetCommand { get; set; }

	public Visibility IsVisResetBut
	{
		get
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			return isVisResetBut;
		}
		set
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			//IL_0002: Unknown result type (might be due to invalid IL or missing references)
			isVisResetBut = value;
			((ObservableObject)this).RaisePropertyChanged("IsVisResetBut");
		}
	}

	public string AppVersion
	{
		get
		{
			return appVersion;
		}
		set
		{
			appVersion = value;
			((ObservableObject)this).RaisePropertyChanged("AppVersion");
		}
	}

	public bool SavePwdCheckBox
	{
		get
		{
			return savePwdCheckBox;
		}
		set
		{
			savePwdCheckBox = value;
			((ObservableObject)this).RaisePropertyChanged("SavePwdCheckBox");
		}
	}

	public Visibility IsVisServer
	{
		get
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			return isVisServer;
		}
		set
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			//IL_0002: Unknown result type (might be due to invalid IL or missing references)
			isVisServer = value;
			((ObservableObject)this).RaisePropertyChanged("IsVisServer");
		}
	}

	public Visibility LoginButVis
	{
		get
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			return loginButVis;
		}
		set
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			//IL_0002: Unknown result type (might be due to invalid IL or missing references)
			loginButVis = value;
			((ObservableObject)this).RaisePropertyChanged("LoginButVis");
		}
	}

	public Visibility LoginButIngVis
	{
		get
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			return loginButIngVis;
		}
		set
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			//IL_0002: Unknown result type (might be due to invalid IL or missing references)
			loginButIngVis = value;
			((ObservableObject)this).RaisePropertyChanged("LoginButIngVis");
		}
	}

	public Visibility LoginLoading
	{
		get
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			return loginLoading;
		}
		set
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			//IL_0002: Unknown result type (might be due to invalid IL or missing references)
			loginLoading = value;
			((ObservableObject)this).RaisePropertyChanged("LoginLoading");
		}
	}

	public LoginViewModel()
	{
		//IL_0002: Unknown result type (might be due to invalid IL or missing references)
		//IL_0009: Unknown result type (might be due to invalid IL or missing references)
		//IL_005d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0038: Unknown result type (might be due to invalid IL or missing references)
		if (AppConfig.getConfig(Constant.INI_APP_ROOT, Constant.INI_APP_SERVER) == "")
		{
			IsVisResetBut = (Visibility)1;
			isVisServer = (Visibility)0;
		}
		else
		{
			IsVisResetBut = (Visibility)0;
			IpAddr = AppConfig.getConfig(Constant.INI_APP_ROOT, Constant.INI_APP_SERVER);
			isVisServer = (Visibility)2;
		}
		UserName = AppConfig.getConfig(Constant.INI_APP_ROOT, Constant.INI_APP_USERNAME);
		Pwd = AppConfig.getConfig(Constant.INI_APP_ROOT, Constant.INI_APP_PASSWORD);
		if (Pwd != "")
		{
			savePwdCheckBox = true;
		}
		LoginCommand = new RelayCommand<Window>((Action<Window>)Login, false);
		ResetCommand = new RelayCommand<Window>((Action<Window>)ResetFunt, false);
		loginButIsDisable = true;
		loginButContent = "立即登录";
		LoginLoading = (Visibility)1;
		AppVersion = "客户端版本号：" + Global.app_version;
	}

	public void Login(Window window)
	{
		//IL_0015: Unknown result type (might be due to invalid IL or missing references)
		//IL_001b: Invalid comparison between Unknown and I4
		if ((int)LoginLoading != 1)
		{
			return;
		}
		new Thread((object st) =>
		{
			//IL_049b: Unknown result type (might be due to invalid IL or missing references)
			//IL_002c: Unknown result type (might be due to invalid IL or missing references)
			//IL_005f: Unknown result type (might be due to invalid IL or missing references)
			//IL_00ad: Unknown result type (might be due to invalid IL or missing references)
			//IL_0092: Unknown result type (might be due to invalid IL or missing references)
			//IL_01a8: Unknown result type (might be due to invalid IL or missing references)
			//IL_01ad: Unknown result type (might be due to invalid IL or missing references)
			//IL_01b3: Unknown result type (might be due to invalid IL or missing references)
			//IL_01bd: Expected Obj, but got Unknown
			//IL_0446: Unknown result type (might be due to invalid IL or missing references)
			//IL_03e2: Unknown result type (might be due to invalid IL or missing references)
			if (IpAddr == null || IpAddr.Length == 0)
			{
				MessageBox.Show("请输入口令地址", "错误提示", (MessageBoxButton)0, (MessageBoxImage)16);
			}
			else if (UserName == null && UserName.Length == 0)
			{
				MessageBox.Show("请输入用户名称", "错误提示", (MessageBoxButton)0, (MessageBoxImage)16);
			}
			else if (pwd == null && pwd.Length == 0)
			{
				MessageBox.Show("请输入用户密码", "错误提示", (MessageBoxButton)0, (MessageBoxImage)16);
			}
			else
			{
				if (IsConnectInternet.IsConnect())
				{
					LoginButIsDisable = false;
					LoginButContent = "校验中...";
					LoginButIngVis = (Visibility)0;
					LoginButVis = (Visibility)2;
					LoginLoading = (Visibility)0;
					HttpClient httpClient = new HttpClient();
					string content = JsonConvert.SerializeObject((object)new Dictionary<string, object>
					{
						["username"] = UserName,
						["password"] = Pwd,
						["devicetype"] = 5
					});
					try
					{
						Uri requestUri = new Uri("http://" + ipAddr + ":8081/api/login/login/distinguish?app_name=" + Global.app_name);
						HttpContent httpContent = new StringContent(content);
						httpContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
						string result = httpClient.PostAsync(requestUri, httpContent).Result.Content.ReadAsStringAsync().Result;
						Dictionary<string, object> dictionary = new Dictionary<string, object>();
						JsonSerializer val = new JsonSerializer();
						((Collection<JsonConverter>)(object)val.Converters).Add((JsonConverter)new JavaScriptDateTimeConverter());
						val.NullValueHandling = (NullValueHandling)1;
						dictionary = JsonConvert.DeserializeObject<Dictionary<string, object>>(result);
						if ((bool)dictionary["success"])
						{
							LoginResp loginResp = new LoginResp();
							loginResp = JsonConvert.DeserializeObject<LoginResp>(result);
							Global.ipAddr = ipAddr;
							Global.jwt = loginResp.Jwt;
							Global.devices = loginResp.Devices;
							Global.loginUser = loginResp.LoginUser;
							Global.rsaKey = CryptionUtils.GetKey();
							content = JsonConvert.SerializeObject((object)new Dictionary<string, object>
							{
								["key"] = Global.rsaKey.PublicKey,
								["client_type"] = 5
							});
							string text = HttpReq.post("server/get_server_addr", content);
							Dictionary<string, object> dictionary2 = new Dictionary<string, object>();
							dictionary2 = JsonConvert.DeserializeObject<Dictionary<string, object>>(text);
							if ((bool)dictionary2["success"])
							{
								LoginLoading = (Visibility)1;
								AppConfig.setConfig(Constant.INI_APP_ROOT, Constant.INI_APP_SERVER, ipAddr);
								AppConfig.setConfig(Constant.INI_APP_ROOT, Constant.INI_APP_USERNAME, UserName);
								AppConfig.setConfig(Constant.INI_APP_ROOT, Constant.INI_APP_PASSWORD, SavePwdCheckBox ? Pwd : "");
								string text2 = CryptionUtils.DecryptByPrivateKey((string)dictionary2["msg"], Global.rsaKey.PrivateKey);
								new List<string>();
								List<string> list = JsonConvert.DeserializeObject<List<string>>(text2);
								Global.ipAddr = list[0];
								Global.dataKey = list[1];
								((DispatcherObject)Application.Current).Dispatcher.Invoke((Action)(() =>
								{
									//IL_0005: Unknown result type (might be due to invalid IL or missing references)
									MessageBox.Show("登录成功");
									((Window)new HomeWindow(loginResp, Global.ipAddr)).Show();
									window.Close();
								}));
							}
							else
							{
								LoginButIngVis = (Visibility)2;
								LoginButVis = (Visibility)0;
								LoginButIsDisable = true;
								LoginButContent = "立即登录";
								LoginLoading = (Visibility)1;
								MessageBox.Show(dictionary["msg"].ToString(), "错误提示", (MessageBoxButton)0, (MessageBoxImage)16);
							}
						}
						else
						{
							LoginButIngVis = (Visibility)2;
							LoginButVis = (Visibility)0;
							LoginButIsDisable = true;
							LoginButContent = "立即登录";
							LoginLoading = (Visibility)1;
							MessageBox.Show(dictionary["msg"].ToString(), "错误提示", (MessageBoxButton)0, (MessageBoxImage)16);
						}
						return;
					}
					catch (Exception ex)
					{
						LoginButIngVis = (Visibility)2;
						LoginButVis = (Visibility)0;
						LoginButIsDisable = true;
						LoginButContent = "立即登录";
						LoginLoading = (Visibility)1;
						MessageBox.Show(ex.Message, "登录异常错误", (MessageBoxButton)0, (MessageBoxImage)16);
						return;
					}
				}
				MessageBox.Show("网络不可以，请您检查网络配置！", "网络错误", (MessageBoxButton)0, (MessageBoxImage)16);
			}
		}).Start();
	}

	private void ResetFunt(Window window)
	{
		//IL_0012: Unknown result type (might be due to invalid IL or missing references)
		//IL_0018: Invalid comparison between Unknown and I4
		//IL_003e: Unknown result type (might be due to invalid IL or missing references)
		if (Global.serverInputVis && (int)MessageBox.Show("请问您是否需要重置口令配置", "询问提示", (MessageBoxButton)4) == 6)
		{
			AppConfig.setConfig(Constant.INI_APP_ROOT, Constant.INI_APP_SERVER, "");
			IpAddr = "";
			MessageBox.Show("重置成功");
			IsVisServer = (Visibility)0;
			IsVisResetBut = (Visibility)1;
			LoginButIngVis = (Visibility)2;
			LoginButVis = (Visibility)0;
		}
	}
}
