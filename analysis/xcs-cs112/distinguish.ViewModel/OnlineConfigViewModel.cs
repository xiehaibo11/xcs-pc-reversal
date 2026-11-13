using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Windows;
using GalaSoft.MvvmLight;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using distinguish.Common;
using distinguish.Model;

namespace distinguish.ViewModel;

public class OnlineConfigViewModel : ViewModelBase
{
	private ConfigNumber configNumber;

	private WifiConfig wifiConfig;

	private DataConfig dataConfig;

	private bool saveConfigNumberButIsDisable;

	private string saveConfigNumberButContent;

	private bool saveDataConfigButIsDisable;

	private string saveDataConfigButContent;

	private bool saveWifiConfigButIsDisable;

	private string saveWifiConfigButContent;

	public ConfigNumber ConfigNumber
	{
		get
		{
			return configNumber;
		}
		set
		{
			configNumber = value;
			((ObservableObject)this).RaisePropertyChanged("ConfigNumber");
		}
	}

	public WifiConfig WifiConfig
	{
		get
		{
			return wifiConfig;
		}
		set
		{
			wifiConfig = value;
			((ObservableObject)this).RaisePropertyChanged("WifiConfig");
		}
	}

	public DataConfig DataConfig
	{
		get
		{
			return dataConfig;
		}
		set
		{
			dataConfig = value;
			((ObservableObject)this).RaisePropertyChanged("DataConfig");
		}
	}

	public bool SaveConfigNumberButIsDisable
	{
		get
		{
			return saveConfigNumberButIsDisable;
		}
		set
		{
			saveConfigNumberButIsDisable = value;
		}
	}

	public string SaveConfigNumberButContent
	{
		get
		{
			return saveConfigNumberButContent;
		}
		set
		{
			saveConfigNumberButContent = value;
		}
	}

	public bool SaveDataConfigButIsDisable
	{
		get
		{
			return saveDataConfigButIsDisable;
		}
		set
		{
			saveDataConfigButIsDisable = value;
		}
	}

	public string SaveDataConfigButContent
	{
		get
		{
			return saveDataConfigButContent;
		}
		set
		{
			saveDataConfigButContent = value;
		}
	}

	public bool SaveWifiConfigButIsDisable
	{
		get
		{
			return saveWifiConfigButIsDisable;
		}
		set
		{
			saveWifiConfigButIsDisable = value;
		}
	}

	public string SaveWifiConfigButContent
	{
		get
		{
			return saveWifiConfigButContent;
		}
		set
		{
			saveWifiConfigButContent = value;
		}
	}

	public OnlineConfigViewModel()
	{
		getServerData();
		getDeviceWifiList();
		saveConfigNumberButContent = "应用张数";
		saveConfigNumberButIsDisable = true;
		SaveDataConfigButContent = "立即应用";
		SaveDataConfigButIsDisable = true;
		SaveWifiConfigButContent = "立即应用";
		SaveWifiConfigButIsDisable = true;
	}

	public bool getServerData()
	{
		//IL_0010: Unknown result type (might be due to invalid IL or missing references)
		//IL_0015: Unknown result type (might be due to invalid IL or missing references)
		//IL_001b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0025: Expected O, but got Unknown
		//IL_00c4: Unknown result type (might be due to invalid IL or missing references)
		string text = HttpReq.get("config/get_config");
		Dictionary<string, object> dictionary = new Dictionary<string, object>();
		JsonSerializer val = new JsonSerializer();
		((Collection<JsonConverter>)(object)val.Converters).Add((JsonConverter)new JavaScriptDateTimeConverter());
		val.NullValueHandling = (NullValueHandling)1;
		dictionary = JsonConvert.DeserializeObject<Dictionary<string, object>>(text);
		if ((bool)dictionary["success"])
		{
			Dictionary<string, object> dictionary2 = JsonConvert.DeserializeObject<Dictionary<string, object>>(dictionary["data"].ToString());
			ConfigNumber = JsonConvert.DeserializeObject<ConfigNumber>(dictionary2["config_number"].ToString());
			WifiConfig = JsonConvert.DeserializeObject<WifiConfig>(dictionary2["wifi_config"].ToString());
			DataConfig = JsonConvert.DeserializeObject<DataConfig>(dictionary2["data_config"].ToString());
			return true;
		}
		MessageBox.Show(dictionary["message"].ToString(), "错误提示", (MessageBoxButton)0, (MessageBoxImage)16);
		return false;
	}

	public void getDeviceWifiList()
	{
		//IL_0010: Unknown result type (might be due to invalid IL or missing references)
		//IL_0015: Unknown result type (might be due to invalid IL or missing references)
		//IL_001b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0025: Expected O, but got Unknown
		string text = HttpReq.get("config/get_device_wifi_list");
		new Dictionary<string, object>();
		JsonSerializer val = new JsonSerializer();
		((Collection<JsonConverter>)(object)val.Converters).Add((JsonConverter)new JavaScriptDateTimeConverter());
		val.NullValueHandling = (NullValueHandling)1;
		_ = (bool)JsonConvert.DeserializeObject<Dictionary<string, object>>(text)["success"];
	}

	public void saveConfigNumber()
	{
		//IL_009a: Unknown result type (might be due to invalid IL or missing references)
		//IL_009f: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a5: Unknown result type (might be due to invalid IL or missing references)
		//IL_00af: Expected O, but got Unknown
		//IL_0245: Unknown result type (might be due to invalid IL or missing references)
		//IL_0214: Unknown result type (might be due to invalid IL or missing references)
		SaveConfigNumberButContent = "保存中...";
		SaveConfigNumberButIsDisable = false;
		string jsonData = JsonConvert.SerializeObject((object)new Dictionary<string, object>
		{
			["east"] = ConfigNumber.East,
			["south"] = ConfigNumber.South,
			["west"] = ConfigNumber.West,
			["north"] = ConfigNumber.North
		});
		string text = HttpReq.post("config/set_number", jsonData);
		Dictionary<string, object> dictionary = new Dictionary<string, object>();
		JsonSerializer val = new JsonSerializer();
		((Collection<JsonConverter>)(object)val.Converters).Add((JsonConverter)new JavaScriptDateTimeConverter());
		val.NullValueHandling = (NullValueHandling)1;
		dictionary = JsonConvert.DeserializeObject<Dictionary<string, object>>(text);
		if ((bool)dictionary["success"])
		{
			foreach (Channel channel in Global.HomeViewModel.Channels)
			{
				switch (channel.Direction)
				{
				case 1:
					channel.Mj_count = ConfigNumber.East;
					break;
				case 2:
					channel.Mj_count = ConfigNumber.South;
					break;
				case 3:
					channel.Mj_count = ConfigNumber.West;
					break;
				case 4:
					channel.Mj_count = ConfigNumber.North;
					break;
				}
			}
			foreach (Device device in Global.devices)
			{
				switch (device.Direction)
				{
				case 1:
					device.Mj_count = ConfigNumber.East;
					break;
				case 2:
					device.Mj_count = ConfigNumber.South;
					break;
				case 3:
					device.Mj_count = ConfigNumber.West;
					break;
				case 4:
					device.Mj_count = ConfigNumber.North;
					break;
				}
			}
			MessageBox.Show("保存成功");
		}
		else
		{
			SaveConfigNumberButContent = "应用张数";
			SaveConfigNumberButIsDisable = true;
			MessageBox.Show(dictionary["msg"].ToString(), "错误提示", (MessageBoxButton)0, (MessageBoxImage)16);
		}
	}

	public void saveDatgaConfig()
	{
		//IL_009a: Unknown result type (might be due to invalid IL or missing references)
		//IL_009f: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a5: Unknown result type (might be due to invalid IL or missing references)
		//IL_00af: Expected O, but got Unknown
		//IL_0142: Unknown result type (might be due to invalid IL or missing references)
		//IL_0111: Unknown result type (might be due to invalid IL or missing references)
		SaveDataConfigButContent = "保存中...";
		SaveDataConfigButIsDisable = false;
		string jsonData = JsonConvert.SerializeObject((object)new Dictionary<string, object>
		{
			["data_send"] = DataConfig.Data_send,
			["timeout"] = DataConfig.Timeout,
			["delay"] = DataConfig.Delay,
			["count_type"] = DataConfig.Count_type
		});
		string text = HttpReq.post("config/set_data_config", jsonData);
		Dictionary<string, object> dictionary = new Dictionary<string, object>();
		JsonSerializer val = new JsonSerializer();
		((Collection<JsonConverter>)(object)val.Converters).Add((JsonConverter)new JavaScriptDateTimeConverter());
		val.NullValueHandling = (NullValueHandling)1;
		dictionary = JsonConvert.DeserializeObject<Dictionary<string, object>>(text);
		if ((bool)dictionary["success"])
		{
			Global.loginUser.DataSend = dataConfig.Data_send;
			Global.loginUser.Timeout = dataConfig.Timeout;
			Global.loginUser.Delay = dataConfig.Delay;
			MessageBox.Show("保存成功");
		}
		else
		{
			SaveDataConfigButContent = "立即保存";
			SaveDataConfigButIsDisable = true;
			MessageBox.Show(dictionary["msg"].ToString(), "错误提示", (MessageBoxButton)0, (MessageBoxImage)16);
		}
	}

	public void saveWifiConfig()
	{
		//IL_005a: Unknown result type (might be due to invalid IL or missing references)
		//IL_005f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0065: Unknown result type (might be due to invalid IL or missing references)
		//IL_006f: Expected O, but got Unknown
		//IL_00c3: Unknown result type (might be due to invalid IL or missing references)
		//IL_0092: Unknown result type (might be due to invalid IL or missing references)
		SaveWifiConfigButContent = "保存中...";
		SaveWifiConfigButIsDisable = false;
		string jsonData = JsonConvert.SerializeObject((object)new Dictionary<string, object>
		{
			["ssid"] = WifiConfig.Ssid,
			["pwd"] = WifiConfig.Pwd
		});
		string text = HttpReq.post("config/set_wifi", jsonData);
		Dictionary<string, object> dictionary = new Dictionary<string, object>();
		JsonSerializer val = new JsonSerializer();
		((Collection<JsonConverter>)(object)val.Converters).Add((JsonConverter)new JavaScriptDateTimeConverter());
		val.NullValueHandling = (NullValueHandling)1;
		dictionary = JsonConvert.DeserializeObject<Dictionary<string, object>>(text);
		if ((bool)dictionary["success"])
		{
			MessageBox.Show("保存成功");
			return;
		}
		SaveWifiConfigButContent = "立即应用";
		SaveWifiConfigButIsDisable = true;
		MessageBox.Show(dictionary["msg"].ToString(), "错误提示", (MessageBoxButton)0, (MessageBoxImage)16);
	}

	public bool restartDevice()
	{
		//IL_0010: Unknown result type (might be due to invalid IL or missing references)
		//IL_0015: Unknown result type (might be due to invalid IL or missing references)
		//IL_001b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0025: Expected O, but got Unknown
		//IL_006e: Unknown result type (might be due to invalid IL or missing references)
		//IL_004e: Unknown result type (might be due to invalid IL or missing references)
		string text = HttpReq.get("zhuo/restart_device");
		Dictionary<string, object> dictionary = new Dictionary<string, object>();
		JsonSerializer val = new JsonSerializer();
		((Collection<JsonConverter>)(object)val.Converters).Add((JsonConverter)new JavaScriptDateTimeConverter());
		val.NullValueHandling = (NullValueHandling)1;
		dictionary = JsonConvert.DeserializeObject<Dictionary<string, object>>(text);
		if ((bool)dictionary["success"])
		{
			MessageBox.Show("重启成功", "消息提示", (MessageBoxButton)0);
			return true;
		}
		MessageBox.Show(dictionary["message"].ToString(), "错误提示", (MessageBoxButton)0, (MessageBoxImage)16);
		return false;
	}
}
