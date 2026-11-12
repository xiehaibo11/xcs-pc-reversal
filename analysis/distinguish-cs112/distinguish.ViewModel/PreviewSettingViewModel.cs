using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Net.Http;
using System.Windows;
using GalaSoft.MvvmLight;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using distinguish.Common;
using distinguish.Model;

namespace distinguish.ViewModel;

public class PreviewSettingViewModel : ViewModelBase
{
	private Device device;

	private int x;

	private int y;

	private int width;

	private int height;

	private int brightness;

	private int motor;

	private string imgPath;

	private int previewId;

	private bool saveButIsDisable;

	private string saveButContent;

	private Visibility dataStatus;

	private string previewName;

	private int capBrightness;

	private int capExposure;

	private int capHue;

	private int capSaturation;

	private int capContrast;

	private int capGain;

	private int imgRotation;

	private int imgMinHreshold;

	private int capFps;

	public Device Device
	{
		get
		{
			return device;
		}
		set
		{
			device = value;
			((ObservableObject)this).RaisePropertyChanged("Device");
		}
	}

	public int X
	{
		get
		{
			return x;
		}
		set
		{
			x = value;
			((ObservableObject)this).RaisePropertyChanged("X");
		}
	}

	public int Y
	{
		get
		{
			return y;
		}
		set
		{
			y = value;
			((ObservableObject)this).RaisePropertyChanged("Y");
		}
	}

	public int Width
	{
		get
		{
			return width;
		}
		set
		{
			width = value;
			((ObservableObject)this).RaisePropertyChanged("Width");
		}
	}

	public int Height
	{
		get
		{
			return height;
		}
		set
		{
			height = value;
			((ObservableObject)this).RaisePropertyChanged("Height");
		}
	}

	public int Brightness
	{
		get
		{
			return brightness;
		}
		set
		{
			brightness = value;
			((ObservableObject)this).RaisePropertyChanged("Brightness");
		}
	}

	public string ImgPath
	{
		get
		{
			return imgPath;
		}
		set
		{
			imgPath = value;
			((ObservableObject)this).RaisePropertyChanged("ImgPath");
		}
	}

	public int PreviewId
	{
		get
		{
			return previewId;
		}
		set
		{
			previewId = value;
			((ObservableObject)this).RaisePropertyChanged("PreviewId");
		}
	}

	public bool SaveButIsDisable
	{
		get
		{
			return saveButIsDisable;
		}
		set
		{
			saveButIsDisable = value;
			((ObservableObject)this).RaisePropertyChanged("SaveButIsDisable");
		}
	}

	public string SaveButContent
	{
		get
		{
			return saveButContent;
		}
		set
		{
			saveButContent = value;
			((ObservableObject)this).RaisePropertyChanged("SaveButContent");
		}
	}

	public Visibility DataStatus
	{
		get
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			return dataStatus;
		}
		set
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			//IL_0002: Unknown result type (might be due to invalid IL or missing references)
			dataStatus = value;
			((ObservableObject)this).RaisePropertyChanged("DataStatus");
		}
	}

	public string PreviewName
	{
		get
		{
			return previewName;
		}
		set
		{
			previewName = value;
			((ObservableObject)this).RaisePropertyChanged("PreviewName");
		}
	}

	public int Motor
	{
		get
		{
			return motor;
		}
		set
		{
			motor = value;
			((ObservableObject)this).RaisePropertyChanged("Motor");
		}
	}

	public int CapBrightness
	{
		get
		{
			return capBrightness;
		}
		set
		{
			capBrightness = value;
			((ObservableObject)this).RaisePropertyChanged("CapBrightness");
		}
	}

	public int CapExposure
	{
		get
		{
			return capExposure;
		}
		set
		{
			capExposure = value;
			((ObservableObject)this).RaisePropertyChanged("CapExposure");
		}
	}

	public int CapHue
	{
		get
		{
			return capHue;
		}
		set
		{
			capHue = value;
			((ObservableObject)this).RaisePropertyChanged("CapHue");
		}
	}

	public int CapSaturation
	{
		get
		{
			return capSaturation;
		}
		set
		{
			capSaturation = value;
			((ObservableObject)this).RaisePropertyChanged("CapSaturation");
		}
	}

	public int CapContrast
	{
		get
		{
			return capContrast;
		}
		set
		{
			capContrast = value;
			((ObservableObject)this).RaisePropertyChanged("CapContrast");
		}
	}

	public int CapGain
	{
		get
		{
			return capGain;
		}
		set
		{
			capGain = value;
			((ObservableObject)this).RaisePropertyChanged("CapGain");
		}
	}

	public int ImgRotation
	{
		get
		{
			return imgRotation;
		}
		set
		{
			imgRotation = value;
			((ObservableObject)this).RaisePropertyChanged("ImgRotation");
		}
	}

	public int ImgMinHreshold
	{
		get
		{
			return imgMinHreshold;
		}
		set
		{
			imgMinHreshold = value;
			((ObservableObject)this).RaisePropertyChanged("ImgMinHreshold");
		}
	}

	public int CapFps
	{
		get
		{
			return capFps;
		}
		set
		{
			capFps = value;
			((ObservableObject)this).RaisePropertyChanged("CapFps");
		}
	}

	public PreviewSettingViewModel()
	{
		SaveButContent = "保存参数";
		SaveButIsDisable = true;
	}

	public bool getServerData(Device device)
	{
		//IL_013f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0144: Unknown result type (might be due to invalid IL or missing references)
		//IL_014a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0154: Expected Obj, but got Unknown
		//IL_018c: Unknown result type (might be due to invalid IL or missing references)
		switch (device.Direction)
		{
		case 1:
			PreviewName = "东方预览";
			break;
		case 2:
			PreviewName = "南方预览";
			break;
		case 3:
			PreviewName = "西方预览";
			break;
		case 4:
			PreviewName = "北方预览";
			break;
		}
		previewId = device.Id;
		Width = (int)device.Right;
		Height = (int)device.Bottom;
		X = (int)device.Left;
		Y = (int)device.Top;
		Brightness = (int)device.Brightness;
		Motor = device.Motor;
		CapBrightness = device.Cap_brightness;
		CapExposure = device.Cap_exposure;
		CapHue = device.Cap_hue;
		CapSaturation = device.Cap_saturation;
		CapContrast = device.Cap_contrast;
		CapGain = device.Cap_gain;
		ImgRotation = device.Img_rotation;
		ImgMinHreshold = device.Img_min_hreshold;
		CapFps = device.Cap_fps;
		this.device = device;
		string text = HttpReq.get("preview/start_preview/" + previewId);
		Dictionary<string, object> dictionary = new Dictionary<string, object>();
		JsonSerializer val = new JsonSerializer();
		((Collection<JsonConverter>)(object)val.Converters).Add((JsonConverter)new JavaScriptDateTimeConverter());
		val.NullValueHandling = (NullValueHandling)1;
		dictionary = JsonConvert.DeserializeObject<Dictionary<string, object>>(text);
		if ((bool)dictionary["success"])
		{
			return true;
		}
		MessageBox.Show(dictionary["message"].ToString(), "错误提示", (MessageBoxButton)0, (MessageBoxImage)16);
		return false;
	}

	public bool endPreview()
	{
		//IL_0025: Unknown result type (might be due to invalid IL or missing references)
		//IL_002a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0030: Unknown result type (might be due to invalid IL or missing references)
		//IL_003a: Expected Obj, but got Unknown
		string text = HttpReq.post("preview/end_preview/" + previewId, "");
		new Dictionary<string, object>();
		JsonSerializer val = new JsonSerializer();
		((Collection<JsonConverter>)(object)val.Converters).Add((JsonConverter)new JavaScriptDateTimeConverter());
		val.NullValueHandling = (NullValueHandling)1;
		JsonConvert.DeserializeObject<Dictionary<string, object>>(text);
		return true;
	}

	public bool saveParams(bool alert)
	{
		//IL_0298: Unknown result type (might be due to invalid IL or missing references)
		//IL_029d: Unknown result type (might be due to invalid IL or missing references)
		//IL_02a3: Unknown result type (might be due to invalid IL or missing references)
		//IL_02ad: Expected Obj, but got Unknown
		//IL_0317: Unknown result type (might be due to invalid IL or missing references)
		//IL_02e5: Unknown result type (might be due to invalid IL or missing references)
		SaveButIsDisable = false;
		SaveButContent = "保存中...";
		new HttpClient();
		Dictionary<string, object> dictionary = new Dictionary<string, object>
		{
			["top"] = Y,
			["left"] = X,
			["right"] = Width,
			["bottom"] = Height,
			["brightness"] = Brightness,
			["motor"] = Motor,
			["cap_brightness"] = capBrightness,
			["cap_exposure"] = capExposure,
			["cap_hue"] = capHue,
			["cap_saturation"] = capSaturation,
			["cap_contrast"] = capContrast,
			["cap_gain"] = capGain,
			["img_rotation"] = imgRotation,
			["img_min_hreshold"] = imgMinHreshold,
			["cap_fps"] = capFps
		};
		device.Top = Y;
		device.Left = X;
		device.Right = Width;
		device.Bottom = Height;
		device.Brightness = Brightness;
		device.Motor = Motor;
		device.Cap_brightness = capBrightness;
		device.Cap_exposure = capExposure;
		device.Cap_hue = capHue;
		device.Cap_saturation = capSaturation;
		device.Cap_contrast = capContrast;
		device.Cap_gain = capGain;
		device.Img_rotation = imgRotation;
		device.Img_min_hreshold = ImgMinHreshold;
		device.Cap_fps = CapFps;
		string text = JsonConvert.SerializeObject((object)dictionary);
		string text2 = HttpReq.post("preview/set_params_v2/" + previewId, text);
		Console.WriteLine(text);
		Dictionary<string, object> dictionary2 = new Dictionary<string, object>();
		JsonSerializer val = new JsonSerializer();
		((Collection<JsonConverter>)(object)val.Converters).Add((JsonConverter)new JavaScriptDateTimeConverter());
		val.NullValueHandling = (NullValueHandling)1;
		dictionary2 = JsonConvert.DeserializeObject<Dictionary<string, object>>(text2);
		if ((bool)dictionary2["success"])
		{
			SaveButIsDisable = true;
			SaveButContent = "保存参数";
			if (alert)
			{
				MessageBox.Show("保存成功");
			}
			return true;
		}
		SaveButIsDisable = true;
		SaveButContent = "保存参数";
		MessageBox.Show(dictionary2["msg"].ToString(), "错误提示", (MessageBoxButton)0, (MessageBoxImage)16);
		return false;
	}
}
