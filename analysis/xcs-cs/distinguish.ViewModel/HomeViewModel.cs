using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using System.Linq;
using System.Media;
using System.Net;
using System.Reflection;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading;
using System.Timers;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Threading;
using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Command;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using WebSocketSharp;
using distinguish.Common;
using distinguish.Model;

namespace distinguish.ViewModel;

public class HomeViewModel : ViewModelBase
{
	private System.Timers.Timer deviceTimer;

	private System.Timers.Timer acceptDataTimer;

	private int timeout;

	public System.Timers.Timer qosTimer;

	private string topStatusText = "等待接受数据";

	private int delayTime;

	private string wsContentStatus;

	private string offineSettingText = "联机配置";

	private string previewText = "画框预览";

	private string startText = "开始游戏";

	private string endText = "结束游戏";

	private string restartText = "重新开始";

	private string validaSendText = "校验发送";

	private string sendText = "立即发送";

	private bool delayTimeError;

	private long delayStartTime;

	private System.Timers.Timer keepaliveTimer;

	private System.Timers.Timer checkWsContentTimer;

	private List<MjConfig> mjConfigList = new List<MjConfig>();

	public TabControl tabControl;

	private Visibility sendDataLoading = (Visibility)1;

	private System.Timers.Timer checkIsConnectInternetTimer;

	private string notConnectInternet = "";

	private string ipAddr;

	private ObservableCollection<Channel> channels;

	private ObservableCollection<Channel> upChannels;

	public bool upSend = true;

	private LoginResp loginResp;

	private double channelHeight;

	private double channelMjHeight;

	private double channelMjWidth;

	private PreviewSettingViewModel previewSettingViewModel;

	private ChannelMjResult selectChannelMjResult;

	private ObservableCollection<ChannelMjResult> wan1 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> wan2 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> wan3 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> wan4 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> wan5 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> wan6 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> wan7 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> wan8 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> wan9 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> tiao1 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> tiao2 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> tiao3 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> tiao4 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> tiao5 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> tiao6 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> tiao7 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> tiao8 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> tiao9 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> tong1 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> tong2 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> tong3 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> tong4 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> tong5 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> tong6 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> tong7 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> tong8 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> tong9 = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> dongFang = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> nan = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> xi = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> bei = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> zhong = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> fa = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> bai = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> baida = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> baipi = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> chun = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> xia = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> qiu = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> dong = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> mei = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> lan = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> ju = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> zhu = new ObservableCollection<ChannelMjResult>();

	private ObservableCollection<ChannelMjResult> x = new ObservableCollection<ChannelMjResult>();

	private int wan1StatusNumber;

	private int wan2StatusNumber;

	private int wan3StatusNumber;

	private int wan4StatusNumber;

	private int wan5StatusNumber;

	private int wan6StatusNumber;

	private int wan7StatusNumber;

	private int wan8StatusNumber;

	private int wan9StatusNumber;

	private int tiao1StatusNumber;

	private int tiao2StatusNumber;

	private int tiao3StatusNumber;

	private int tiao4StatusNumber;

	private int tiao5StatusNumber;

	private int tiao6StatusNumber;

	private int tiao7StatusNumber;

	private int tiao8StatusNumber;

	private int tiao9StatusNumber;

	private int tong1StatusNumber;

	private int tong2StatusNumber;

	private int tong3StatusNumber;

	private int tong4StatusNumber;

	private int tong5StatusNumber;

	private int tong6StatusNumber;

	private int tong7StatusNumber;

	private int tong8StatusNumber;

	private int tong9StatusNumber;

	private int dongFangStatusNumber;

	private int nanStatusNumber;

	private int xiStatusNumber;

	private int beiStatusNumber;

	private int zhongStatusNumber;

	private int faStatusNumber;

	private int baiStatusNumber;

	private int baidaStatusNumber;

	private int baipiStatusNumber;

	private RelayCommand<ChannelMjResult> validMjClick;

	private string lastSendTimeStr = "";

	private int sendSumNumber;

	private int manuallySendNumber;

	private int autoSendNumber;

	private string sendDataTotalStr = "";

	private bool sendVlidateIsCheck = true;

	private ObservableCollection<SendDataLog> sendDataLogList = new ObservableCollection<SendDataLog>();

	public string TopStatusText
	{
		get
		{
			return topStatusText;
		}
		set
		{
			topStatusText = value;
			((ObservableObject)this).RaisePropertyChanged("TopStatusText");
		}
	}

	public string WsContentStatus
	{
		get
		{
			return wsContentStatus;
		}
		set
		{
			wsContentStatus = value;
			((ObservableObject)this).RaisePropertyChanged("WsContentStatus");
		}
	}

	public string OffineSettingText
	{
		get
		{
			return offineSettingText;
		}
		set
		{
			offineSettingText = value;
			((ObservableObject)this).RaisePropertyChanged("OffineSettingText");
		}
	}

	public string PreviewText
	{
		get
		{
			return previewText;
		}
		set
		{
			previewText = value;
			((ObservableObject)this).RaisePropertyChanged("PreviewText");
		}
	}

	public string StartText
	{
		get
		{
			return startText;
		}
		set
		{
			startText = value;
			((ObservableObject)this).RaisePropertyChanged("StartText");
		}
	}

	public string EndText
	{
		get
		{
			return endText;
		}
		set
		{
			endText = value;
			((ObservableObject)this).RaisePropertyChanged("EndText");
		}
	}

	public string RestartText
	{
		get
		{
			return restartText;
		}
		set
		{
			restartText = value;
			((ObservableObject)this).RaisePropertyChanged("RestartText");
		}
	}

	public string ValidaSendText
	{
		get
		{
			return validaSendText;
		}
		set
		{
			validaSendText = value;
			((ObservableObject)this).RaisePropertyChanged("ValidaSendText");
		}
	}

	public string SendText
	{
		get
		{
			return sendText;
		}
		set
		{
			sendText = value;
			((ObservableObject)this).RaisePropertyChanged("SendText");
		}
	}

	public int DelayTime
	{
		get
		{
			return delayTime;
		}
		set
		{
			delayTime = value;
			((ObservableObject)this).RaisePropertyChanged("DelayTime");
		}
	}

	public bool DelayTimeError
	{
		get
		{
			return delayTimeError;
		}
		set
		{
			delayTimeError = value;
			((ObservableObject)this).RaisePropertyChanged("DelayTimeError");
		}
	}

	public long DelayStartTime
	{
		get
		{
			return delayStartTime;
		}
		set
		{
			delayStartTime = value;
			((ObservableObject)this).RaisePropertyChanged("DelayStartTime");
		}
	}

	public Visibility SendDataLoading
	{
		get
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			return sendDataLoading;
		}
		set
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			//IL_0002: Unknown result type (might be due to invalid IL or missing references)
			sendDataLoading = value;
			((ObservableObject)this).RaisePropertyChanged("SendDataLoading");
		}
	}

	public string NotConnectInternet
	{
		get
		{
			return notConnectInternet;
		}
		set
		{
			notConnectInternet = value;
			((ObservableObject)this).RaisePropertyChanged("NotConnectInternet");
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

	public ObservableCollection<Channel> Channels
	{
		get
		{
			return channels;
		}
		set
		{
			channels = value;
			((ObservableObject)this).RaisePropertyChanged("Channels");
		}
	}

	public ObservableCollection<Channel> UpChannels
	{
		get
		{
			return upChannels;
		}
		set
		{
			upChannels = value;
			((ObservableObject)this).RaisePropertyChanged("UpChannels");
		}
	}

	public LoginResp LoginResp
	{
		get
		{
			return loginResp;
		}
		set
		{
			loginResp = value;
			((ObservableObject)this).RaisePropertyChanged("LoginResp");
		}
	}

	public double ChannelHeight
	{
		get
		{
			return channelHeight;
		}
		set
		{
			channelHeight = value;
			((ObservableObject)this).RaisePropertyChanged("ChannelHeight");
		}
	}

	public double ChannelMjHeight
	{
		get
		{
			return channelMjHeight;
		}
		set
		{
			channelMjHeight = value;
			((ObservableObject)this).RaisePropertyChanged("ChannelMjHeight");
		}
	}

	public double ChannelMjWidth
	{
		get
		{
			return channelMjWidth;
		}
		set
		{
			channelMjWidth = value;
			((ObservableObject)this).RaisePropertyChanged("ChannelMjWidth");
		}
	}

	public PreviewSettingViewModel PreviewSettingViewModel
	{
		get
		{
			return previewSettingViewModel;
		}
		set
		{
			previewSettingViewModel = value;
		}
	}

	public RelayCommand<string> RightChangeCommand { get; set; }

	public ChannelMjResult SelectChannelMjResult
	{
		get
		{
			return selectChannelMjResult;
		}
		set
		{
			selectChannelMjResult = value;
			((ObservableObject)this).RaisePropertyChanged("SelectChannelMjResult");
		}
	}

	public ObservableCollection<ChannelMjResult> Wan1
	{
		get
		{
			return wan1;
		}
		set
		{
			wan1 = value;
			((ObservableObject)this).RaisePropertyChanged("Wan1");
		}
	}

	public ObservableCollection<ChannelMjResult> Wan2
	{
		get
		{
			return wan2;
		}
		set
		{
			wan2 = value;
			((ObservableObject)this).RaisePropertyChanged("Wan2");
		}
	}

	public ObservableCollection<ChannelMjResult> Wan3
	{
		get
		{
			return wan3;
		}
		set
		{
			wan3 = value;
			((ObservableObject)this).RaisePropertyChanged("Wan3");
		}
	}

	public ObservableCollection<ChannelMjResult> Wan4
	{
		get
		{
			return wan4;
		}
		set
		{
			wan4 = value;
			((ObservableObject)this).RaisePropertyChanged("Wan4");
		}
	}

	public ObservableCollection<ChannelMjResult> Wan5
	{
		get
		{
			return wan5;
		}
		set
		{
			wan5 = value;
			((ObservableObject)this).RaisePropertyChanged("Wan5");
		}
	}

	public ObservableCollection<ChannelMjResult> Wan6
	{
		get
		{
			return wan6;
		}
		set
		{
			wan6 = value;
			((ObservableObject)this).RaisePropertyChanged("Wan6");
		}
	}

	public ObservableCollection<ChannelMjResult> Wan7
	{
		get
		{
			return wan7;
		}
		set
		{
			wan7 = value;
			((ObservableObject)this).RaisePropertyChanged("Wan7");
		}
	}

	public ObservableCollection<ChannelMjResult> Wan8
	{
		get
		{
			return wan8;
		}
		set
		{
			wan8 = value;
			((ObservableObject)this).RaisePropertyChanged("Wan8");
		}
	}

	public ObservableCollection<ChannelMjResult> Wan9
	{
		get
		{
			return wan9;
		}
		set
		{
			wan9 = value;
			((ObservableObject)this).RaisePropertyChanged("Wan9");
		}
	}

	public ObservableCollection<ChannelMjResult> Tiao1
	{
		get
		{
			return tiao1;
		}
		set
		{
			tiao1 = value;
			((ObservableObject)this).RaisePropertyChanged("Tiao1");
		}
	}

	public ObservableCollection<ChannelMjResult> Tiao2
	{
		get
		{
			return tiao2;
		}
		set
		{
			tiao2 = value;
			((ObservableObject)this).RaisePropertyChanged("Tiao2");
		}
	}

	public ObservableCollection<ChannelMjResult> Tiao3
	{
		get
		{
			return tiao3;
		}
		set
		{
			tiao3 = value;
			((ObservableObject)this).RaisePropertyChanged("Tiao3");
		}
	}

	public ObservableCollection<ChannelMjResult> Tiao4
	{
		get
		{
			return tiao4;
		}
		set
		{
			tiao4 = value;
			((ObservableObject)this).RaisePropertyChanged("Tiao4");
		}
	}

	public ObservableCollection<ChannelMjResult> Tiao5
	{
		get
		{
			return tiao5;
		}
		set
		{
			tiao5 = value;
			((ObservableObject)this).RaisePropertyChanged("Tiao5");
		}
	}

	public ObservableCollection<ChannelMjResult> Tiao6
	{
		get
		{
			return tiao6;
		}
		set
		{
			tiao6 = value;
			((ObservableObject)this).RaisePropertyChanged("Tiao6");
		}
	}

	public ObservableCollection<ChannelMjResult> Tiao7
	{
		get
		{
			return tiao7;
		}
		set
		{
			tiao7 = value;
			((ObservableObject)this).RaisePropertyChanged("Tiao7");
		}
	}

	public ObservableCollection<ChannelMjResult> Tiao8
	{
		get
		{
			return tiao8;
		}
		set
		{
			tiao8 = value;
			((ObservableObject)this).RaisePropertyChanged("Tiao8");
		}
	}

	public ObservableCollection<ChannelMjResult> Tiao9
	{
		get
		{
			return tiao9;
		}
		set
		{
			tiao9 = value;
			((ObservableObject)this).RaisePropertyChanged("Tiao9");
		}
	}

	public ObservableCollection<ChannelMjResult> Tong1
	{
		get
		{
			return tong1;
		}
		set
		{
			tong1 = value;
			((ObservableObject)this).RaisePropertyChanged("Tong1");
		}
	}

	public ObservableCollection<ChannelMjResult> Tong2
	{
		get
		{
			return tong2;
		}
		set
		{
			tong2 = value;
			((ObservableObject)this).RaisePropertyChanged("Tong2");
		}
	}

	public ObservableCollection<ChannelMjResult> Tong3
	{
		get
		{
			return tong3;
		}
		set
		{
			tong3 = value;
			((ObservableObject)this).RaisePropertyChanged("Tong3");
		}
	}

	public ObservableCollection<ChannelMjResult> Tong4
	{
		get
		{
			return tong4;
		}
		set
		{
			tong4 = value;
			((ObservableObject)this).RaisePropertyChanged("Tong4");
		}
	}

	public ObservableCollection<ChannelMjResult> Tong5
	{
		get
		{
			return tong5;
		}
		set
		{
			tong5 = value;
			((ObservableObject)this).RaisePropertyChanged("Tong5");
		}
	}

	public ObservableCollection<ChannelMjResult> Tong6
	{
		get
		{
			return tong6;
		}
		set
		{
			tong6 = value;
			((ObservableObject)this).RaisePropertyChanged("Tong6");
		}
	}

	public ObservableCollection<ChannelMjResult> Tong7
	{
		get
		{
			return tong7;
		}
		set
		{
			tong7 = value;
			((ObservableObject)this).RaisePropertyChanged("Tong7");
		}
	}

	public ObservableCollection<ChannelMjResult> Tong8
	{
		get
		{
			return tong8;
		}
		set
		{
			tong8 = value;
			((ObservableObject)this).RaisePropertyChanged("Tong8");
		}
	}

	public ObservableCollection<ChannelMjResult> Tong9
	{
		get
		{
			return tong9;
		}
		set
		{
			tong9 = value;
			((ObservableObject)this).RaisePropertyChanged("Tong9");
		}
	}

	public ObservableCollection<ChannelMjResult> DongFang
	{
		get
		{
			return dongFang;
		}
		set
		{
			dongFang = value;
			((ObservableObject)this).RaisePropertyChanged("DongFang");
		}
	}

	public ObservableCollection<ChannelMjResult> Nan
	{
		get
		{
			return nan;
		}
		set
		{
			nan = value;
			((ObservableObject)this).RaisePropertyChanged("Nan");
		}
	}

	public ObservableCollection<ChannelMjResult> Xi
	{
		get
		{
			return xi;
		}
		set
		{
			xi = value;
			((ObservableObject)this).RaisePropertyChanged("Xi");
		}
	}

	public ObservableCollection<ChannelMjResult> Bei
	{
		get
		{
			return bei;
		}
		set
		{
			bei = value;
			((ObservableObject)this).RaisePropertyChanged("Bei");
		}
	}

	public ObservableCollection<ChannelMjResult> Zhong
	{
		get
		{
			return zhong;
		}
		set
		{
			zhong = value;
			((ObservableObject)this).RaisePropertyChanged("Zhong");
		}
	}

	public ObservableCollection<ChannelMjResult> Fa
	{
		get
		{
			return fa;
		}
		set
		{
			fa = value;
			((ObservableObject)this).RaisePropertyChanged("Fa");
		}
	}

	public ObservableCollection<ChannelMjResult> Bai
	{
		get
		{
			return bai;
		}
		set
		{
			bai = value;
			((ObservableObject)this).RaisePropertyChanged("Bai");
		}
	}

	public ObservableCollection<ChannelMjResult> Baida
	{
		get
		{
			return baida;
		}
		set
		{
			baida = value;
			((ObservableObject)this).RaisePropertyChanged("Baida");
		}
	}

	public ObservableCollection<ChannelMjResult> Baipi
	{
		get
		{
			return baipi;
		}
		set
		{
			baipi = value;
			((ObservableObject)this).RaisePropertyChanged("Baipi");
		}
	}

	public ObservableCollection<ChannelMjResult> Chun
	{
		get
		{
			return chun;
		}
		set
		{
			chun = value;
			((ObservableObject)this).RaisePropertyChanged("Chun");
		}
	}

	public ObservableCollection<ChannelMjResult> Xia
	{
		get
		{
			return xia;
		}
		set
		{
			xia = value;
			((ObservableObject)this).RaisePropertyChanged("Xia");
		}
	}

	public ObservableCollection<ChannelMjResult> Qiu
	{
		get
		{
			return qiu;
		}
		set
		{
			qiu = value;
			((ObservableObject)this).RaisePropertyChanged("Qiu");
		}
	}

	public ObservableCollection<ChannelMjResult> Dong
	{
		get
		{
			return dong;
		}
		set
		{
			dong = value;
			((ObservableObject)this).RaisePropertyChanged("Dong");
		}
	}

	public ObservableCollection<ChannelMjResult> Mei
	{
		get
		{
			return mei;
		}
		set
		{
			mei = value;
			((ObservableObject)this).RaisePropertyChanged("Mei");
		}
	}

	public ObservableCollection<ChannelMjResult> Lan
	{
		get
		{
			return lan;
		}
		set
		{
			lan = value;
			((ObservableObject)this).RaisePropertyChanged("Lan");
		}
	}

	public ObservableCollection<ChannelMjResult> Ju
	{
		get
		{
			return ju;
		}
		set
		{
			ju = value;
			((ObservableObject)this).RaisePropertyChanged("Ju");
		}
	}

	public ObservableCollection<ChannelMjResult> Zhu
	{
		get
		{
			return zhu;
		}
		set
		{
			zhu = value;
			((ObservableObject)this).RaisePropertyChanged("Zhu");
		}
	}

	public ObservableCollection<ChannelMjResult> X
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

	public int Wan1StatusNumber
	{
		get
		{
			return wan1StatusNumber;
		}
		set
		{
			wan1StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Wan1StatusNumber");
		}
	}

	public int Wan2StatusNumber
	{
		get
		{
			return wan2StatusNumber;
		}
		set
		{
			wan2StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Wan2StatusNumber");
		}
	}

	public int Wan3StatusNumber
	{
		get
		{
			return wan3StatusNumber;
		}
		set
		{
			wan3StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Wan3StatusNumber");
		}
	}

	public int Wan4StatusNumber
	{
		get
		{
			return wan4StatusNumber;
		}
		set
		{
			wan4StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Wan4StatusNumber");
		}
	}

	public int Wan5StatusNumber
	{
		get
		{
			return wan5StatusNumber;
		}
		set
		{
			wan5StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Wan5StatusNumber");
		}
	}

	public int Wan6StatusNumber
	{
		get
		{
			return wan6StatusNumber;
		}
		set
		{
			wan6StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Wan6StatusNumber");
		}
	}

	public int Wan7StatusNumber
	{
		get
		{
			return wan7StatusNumber;
		}
		set
		{
			wan7StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Wan7StatusNumber");
		}
	}

	public int Wan8StatusNumber
	{
		get
		{
			return wan8StatusNumber;
		}
		set
		{
			wan8StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Wan8StatusNumber");
		}
	}

	public int Wan9StatusNumber
	{
		get
		{
			return wan9StatusNumber;
		}
		set
		{
			wan9StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Wan9StatusNumber");
		}
	}

	public int Tiao1StatusNumber
	{
		get
		{
			return tiao1StatusNumber;
		}
		set
		{
			tiao1StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Tiao1StatusNumber");
		}
	}

	public int Tiao2StatusNumber
	{
		get
		{
			return tiao2StatusNumber;
		}
		set
		{
			tiao2StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Tiao2StatusNumber");
		}
	}

	public int Tiao3StatusNumber
	{
		get
		{
			return tiao3StatusNumber;
		}
		set
		{
			tiao3StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Tiao3StatusNumber");
		}
	}

	public int Tiao4StatusNumber
	{
		get
		{
			return tiao4StatusNumber;
		}
		set
		{
			tiao4StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Tiao4StatusNumber");
		}
	}

	public int Tiao5StatusNumber
	{
		get
		{
			return tiao5StatusNumber;
		}
		set
		{
			tiao5StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Tiao5StatusNumber");
		}
	}

	public int Tiao6StatusNumber
	{
		get
		{
			return tiao6StatusNumber;
		}
		set
		{
			tiao6StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Tiao6StatusNumber");
		}
	}

	public int Tiao7StatusNumber
	{
		get
		{
			return tiao7StatusNumber;
		}
		set
		{
			tiao7StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Tiao7StatusNumber");
		}
	}

	public int Tiao8StatusNumber
	{
		get
		{
			return tiao8StatusNumber;
		}
		set
		{
			tiao8StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Tiao8StatusNumber");
		}
	}

	public int Tiao9StatusNumber
	{
		get
		{
			return tiao9StatusNumber;
		}
		set
		{
			tiao9StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Tiao9StatusNumber");
		}
	}

	public int Tong1StatusNumber
	{
		get
		{
			return tong1StatusNumber;
		}
		set
		{
			tong1StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Tong1StatusNumber");
		}
	}

	public int Tong2StatusNumber
	{
		get
		{
			return tong2StatusNumber;
		}
		set
		{
			tong2StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Tong2StatusNumber");
		}
	}

	public int Tong3StatusNumber
	{
		get
		{
			return tong3StatusNumber;
		}
		set
		{
			tong3StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Tong3StatusNumber");
		}
	}

	public int Tong4StatusNumber
	{
		get
		{
			return tong4StatusNumber;
		}
		set
		{
			tong4StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Tong4StatusNumber");
		}
	}

	public int Tong5StatusNumber
	{
		get
		{
			return tong5StatusNumber;
		}
		set
		{
			tong5StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Tong5StatusNumber");
		}
	}

	public int Tong6StatusNumber
	{
		get
		{
			return tong6StatusNumber;
		}
		set
		{
			tong6StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Tong6StatusNumber");
		}
	}

	public int Tong7StatusNumber
	{
		get
		{
			return tong7StatusNumber;
		}
		set
		{
			tong7StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Tong7StatusNumber");
		}
	}

	public int Tong8StatusNumber
	{
		get
		{
			return tong8StatusNumber;
		}
		set
		{
			tong8StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Tong8StatusNumber");
		}
	}

	public int Tong9StatusNumber
	{
		get
		{
			return tong9StatusNumber;
		}
		set
		{
			tong9StatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("Tong9StatusNumber");
		}
	}

	public int DongFangStatusNumber
	{
		get
		{
			return dongFangStatusNumber;
		}
		set
		{
			dongFangStatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("DongFangStatusNumber");
		}
	}

	public int NanStatusNumber
	{
		get
		{
			return nanStatusNumber;
		}
		set
		{
			nanStatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("NanStatusNumber");
		}
	}

	public int XiStatusNumber
	{
		get
		{
			return xiStatusNumber;
		}
		set
		{
			xiStatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("XiStatusNumber");
		}
	}

	public int BeiStatusNumber
	{
		get
		{
			return beiStatusNumber;
		}
		set
		{
			beiStatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("BeiStatusNumber");
		}
	}

	public int ZhongStatusNumber
	{
		get
		{
			return zhongStatusNumber;
		}
		set
		{
			zhongStatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("ZhongStatusNumber");
		}
	}

	public int FaStatusNumber
	{
		get
		{
			return faStatusNumber;
		}
		set
		{
			faStatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("FaStatusNumber");
		}
	}

	public int BaiStatusNumber
	{
		get
		{
			return baiStatusNumber;
		}
		set
		{
			baiStatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("BaiStatusNumber");
		}
	}

	public int BaidaStatusNumber
	{
		get
		{
			return baidaStatusNumber;
		}
		set
		{
			baidaStatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("BaidaStatusNumber");
		}
	}

	public int BaipiStatusNumber
	{
		get
		{
			return baipiStatusNumber;
		}
		set
		{
			baipiStatusNumber = value;
			((ObservableObject)this).RaisePropertyChanged("BaipiStatusNumber");
		}
	}

	public RelayCommand<ChannelMjResult> ValidMjClick
	{
		get
		{
			if (validMjClick == null)
			{
				validMjClick = new RelayCommand<ChannelMjResult>((Action<ChannelMjResult>)delegate(ChannelMjResult p)
				{
					ExecutValidMj(p);
				}, false);
			}
			return validMjClick;
		}
		set
		{
			validMjClick = value;
		}
	}

	public ObservableCollection<SendDataLog> SendDataLogList
	{
		get
		{
			return sendDataLogList;
		}
		set
		{
			sendDataLogList = value;
			((ObservableObject)this).RaisePropertyChanged("SendDataLogList");
		}
	}

	public int SendSumNumber
	{
		get
		{
			return sendSumNumber;
		}
		set
		{
			sendSumNumber = value;
			((ObservableObject)this).RaisePropertyChanged("SendSumNumber");
		}
	}

	public int ManuallySendNumber
	{
		get
		{
			return manuallySendNumber;
		}
		set
		{
			manuallySendNumber = value;
			((ObservableObject)this).RaisePropertyChanged("ManuallySendNumber");
		}
	}

	public int AutoSendNumber
	{
		get
		{
			return autoSendNumber;
		}
		set
		{
			autoSendNumber = value;
			((ObservableObject)this).RaisePropertyChanged("AutoSendNumber");
		}
	}

	public string LastSendTimeStr
	{
		get
		{
			return lastSendTimeStr;
		}
		set
		{
			lastSendTimeStr = value;
			((ObservableObject)this).RaisePropertyChanged("LastSendTimeStr");
		}
	}

	public string SendDataTotalStr
	{
		get
		{
			return sendDataTotalStr;
		}
		set
		{
			sendDataTotalStr = value;
			((ObservableObject)this).RaisePropertyChanged("SendDataTotalStr");
		}
	}

	public bool SendVlidateIsCheck
	{
		get
		{
			return sendVlidateIsCheck;
		}
		set
		{
			sendVlidateIsCheck = value;
			((ObservableObject)this).RaisePropertyChanged("SendVlidateIsCheck");
		}
	}

	[DllImport("Myclassification.dll", CallingConvention = CallingConvention.Cdecl)]
	public static extern string shibie(string params1, string params2, string params3, string params4, string params5);

	public HomeViewModel(LoginResp loginResp, string ipAddr)
	{
		//IL_0065: Unknown result type (might be due to invalid IL or missing references)
		HomeViewModel homeViewModel = this;
		checkAppVersion();
		startCleanQosTimer();
		getMjServerData();
		LoginResp = loginResp;
		IpAddr = ipAddr;
		Channels = new ObservableCollection<Channel>();
		UpChannels = new ObservableCollection<Channel>();
		Channel channel = new Channel("east:")
		{
			Id = 0,
			Direction = 1,
			DirectionName = "东方",
			Online = 1,
			Run = 0,
			RunStr = "灯光"
		};
		Channels.Add(channel);
		Channel channel2 = new Channel("east:")
		{
			Direction = 1
		};
		UpChannels.Add(channel2);
		Channel channel3 = new Channel("south:")
		{
			Id = 1,
			Direction = 2,
			DirectionName = "南方",
			Run = 0,
			RunStr = "灯光"
		};
		Channels.Add(channel3);
		Channel channel4 = new Channel("south:")
		{
			Direction = 2
		};
		UpChannels.Add(channel4);
		Channel channel5 = new Channel("west:")
		{
			Id = 2,
			Direction = 3,
			DirectionName = "西方",
			Run = 0,
			RunStr = "灯光"
		};
		Channels.Add(channel5);
		Channel channel6 = new Channel("west:")
		{
			Direction = 3
		};
		UpChannels.Add(channel6);
		Channel channel7 = new Channel("north:")
		{
			Id = 4,
			Direction = 4,
			DirectionName = "北方",
			Run = 0,
			RunStr = "灯光"
		};
		Channels.Add(channel7);
		Channel channel8 = new Channel("north:")
		{
			Direction = 4
		};
		UpChannels.Add(channel8);
		foreach (Device device in LoginResp.Devices)
		{
			if (device.Direction == 1)
			{
				channel.Mj_count = device.Mj_count;
				channel.Id = device.Id;
				channel.DirectionName = channel.DirectionName + "(" + device.Mj_count + ")";
				channel.Serial_number = device.Serial_no;
				channel.Direction = device.Direction;
				channel2.Mj_count = device.Mj_count;
				channel2.Direction = device.Direction;
				channel2.Serial_number = device.Serial_no;
				channel.setMjNumber("east:", device.Mj_count / 2);
				channel2.setMjNumber("east:", device.Mj_count / 2);
			}
			else if (device.Direction == 2)
			{
				channel3.Mj_count = device.Mj_count;
				channel3.Id = device.Id;
				channel3.DirectionName = channel3.DirectionName + "(" + device.Mj_count + ")";
				channel3.Serial_number = device.Serial_no;
				channel3.Direction = device.Direction;
				channel4.Mj_count = device.Mj_count;
				channel4.Direction = device.Direction;
				channel4.Serial_number = device.Serial_no;
				channel3.setMjNumber("south:", device.Mj_count / 2);
				channel4.setMjNumber("south:", device.Mj_count / 2);
			}
			else if (device.Direction == 3)
			{
				channel5.Mj_count = device.Mj_count;
				channel5.Id = device.Id;
				channel5.DirectionName = channel5.DirectionName + "(" + device.Mj_count + ")";
				channel5.Serial_number = device.Serial_no;
				channel5.Direction = device.Direction;
				channel6.Mj_count = device.Mj_count;
				channel6.Direction = device.Direction;
				channel6.Serial_number = device.Serial_no;
				channel5.setMjNumber("west:", device.Mj_count / 2);
				channel6.setMjNumber("west:", device.Mj_count / 2);
			}
			else if (device.Direction == 4)
			{
				channel7.Mj_count = device.Mj_count;
				channel7.Id = device.Id;
				channel7.DirectionName = channel7.DirectionName + "(" + device.Mj_count + ")";
				channel7.Serial_number = device.Serial_no;
				channel7.Direction = device.Direction;
				channel8.Mj_count = device.Mj_count;
				channel8.Direction = device.Direction;
				channel8.Serial_number = device.Serial_no;
				channel7.setMjNumber("north:", device.Mj_count / 2);
				channel8.setMjNumber("north:", device.Mj_count / 2);
			}
		}
		RightChangeCommand = new RelayCommand<string>((Action<string>)RightChangeBut, false);
		new Thread((ThreadStart)delegate
		{
			NetManager.IPAddress = "ws://" + ipAddr + ":18889/";
			NetManager.Init();
			NetManager.OnMessage = homeViewModel.OnMessage;
			NetManager.Connect(homeViewModel.getWebStockLoginData());
		}).Start();
		previewSettingViewModel = new PreviewSettingViewModel();
		startDeviceStatusTimer();
		SelectAllDeviceStatus();
		startKeepaliveTimer();
		startUpdateWsContent();
		startCheckIsConnectInternetTimer();
		int processorCount = Environment.ProcessorCount;
		ThreadPool.SetMaxThreads(processorCount - 1, processorCount - 1);
		initText();
	}

	public void initText()
	{
		if (Global.app_name == "xcs")
		{
			OffineSettingText = "设置";
			PreviewText = "画框";
			StartText = "开始";
			EndText = "结束";
			RestartText = "重置";
			ValidaSendText = "改牌";
			SendText = "发送";
		}
	}

	public string getWebStockLoginData()
	{
		return JsonConvert.SerializeObject((object)new Protocol
		{
			types = Constant.PROTOCOL_TYPE_C_FROM_CLIENT_TYPE_OF_LOGIN,
			data_content = loginResp.Jwt,
			from = "",
			to = "",
			fp = Guid.NewGuid().ToString(),
			qos = false,
			retry_count = 0
		});
	}

	private void OnMessage(string msg)
	{
		new Thread((ThreadStart)delegate
		{
			OnMessageThread(msg);
		}).Start();
	}

	private void OnMessageThread(string msg)
	{
		Protocol protocol = JsonConvert.DeserializeObject<Protocol>(msg);
		if (protocol.qos && protocol.fp != null)
		{
			if (NetManager.qosList.ContainsKey(protocol.fp))
			{
				return;
			}
			Console.WriteLine("QOS SAVEING");
			NetManager.qosList.Add(protocol.fp, DateTimeOffset.UtcNow.ToUnixTimeMilliseconds());
			new Thread((ThreadStart)delegate
			{
				NetManager.Send(JsonConvert.SerializeObject((object)new Protocol
				{
					from = protocol.to,
					to = protocol.from,
					data_content = protocol.fp,
					types = Constant.PROTOCOL_TYPE_C_FROM_CLIENT_TYPE_OF_RECIVED
				}));
			}).Start();
		}
		if (protocol.types == Constant.PROTOCOL_TYPE_C_FROM_CLIENT_TYPE_OF_COMMON_DATA)
		{
			Dictionary<string, object> dictionary = JsonConvert.DeserializeObject<Dictionary<string, object>>(msg);
			Dictionary<string, object> msg_json_data = JsonConvert.DeserializeObject<Dictionary<string, object>>(dictionary["data_content"].ToString());
			switch (int.Parse(msg_json_data["msg_type"].ToString()))
			{
			case 101:
				ThreadPool.QueueUserWorkItem(delegate
				{
					OnMessageThreadImg(protocol, msg_json_data["msg_content"].ToString());
				});
				break;
			case 102:
			{
				previewSettingViewModel.DataStatus = (Visibility)1;
				string text = AppDomain.CurrentDomain.BaseDirectory + "cacheImg\\";
				string text2 = new Random().Next(1000) + ".jpg";
				string imgPath = text + text2;
				string text3 = msg_json_data["msg_content"].ToString();
				if (-1 != text3.IndexOf("|"))
				{
					string[] array2 = text3.ToString().Split(new char[1] { '|' });
					string s = array2[0];
					string bash = array2[1];
					if (PreviewSettingViewModel.Device != null && PreviewSettingViewModel.Device.Id == int.Parse(s))
					{
						Base64ToImage(Global.GetDataBash64(bash), text, text2);
						PreviewSettingViewModel.ImgPath = imgPath;
					}
				}
				else
				{
					Base64ToImage(Global.GetDataBash64(text3), text, text2);
					PreviewSettingViewModel.ImgPath = imgPath;
				}
				break;
			}
			case 113:
			{
				string[] array = msg_json_data["msg_content"].ToString().Split(new char[1] { '|' });
				int index = 0;
				if (upChannels[0].TopMj.Count > 0)
				{
					index = 0;
				}
				else if (upChannels[1].TopMj.Count > 0)
				{
					index = 1;
				}
				else if (upChannels[2].TopMj.Count > 0)
				{
					index = 2;
				}
				else if (upChannels[3].TopMj.Count > 0)
				{
					index = 3;
				}
				if (upChannels[index].TopMj[0].UploadImg != null && upChannels[index].TopMj[0].UploadImg != "/res/bottom_bg.jpg")
				{
					upSend = false;
				}
				foreach (Channel channel in Channels)
				{
					if (!(channel.Serial_number == array[0]))
					{
						continue;
					}
					channel.Run = int.Parse(array[1]);
					foreach (ChannelMjResult item in channel.TopMj)
					{
						CleanMjItem(item);
					}
					foreach (ChannelMjResult item2 in channel.BottomMj)
					{
						CleanMjItem(item2);
					}
				}
				startAcceptDataTimer();
				break;
			}
			case 114:
			{
				msg_json_data["msg_content"].ToString();
				bool flag = true;
				foreach (Channel channel2 in Channels)
				{
					if (channel2.Serial_number == msg_json_data["msg_content"].ToString())
					{
						channel2.Run = 0;
					}
					if (channel2.Run != 0)
					{
						flag = false;
					}
				}
				if (!flag)
				{
					break;
				}
				acceptDataTimer?.Stop();
				acceptDataTimer = null;
				TopStatusText = "接受完成，等待下轮数据...";
				{
					foreach (Channel channel3 in Channels)
					{
						channel3.DeleteOffset = 0;
					}
					break;
				}
			}
			case 115:
			{
				foreach (Channel channel4 in Channels)
				{
					if (channel4.Serial_number == msg_json_data["msg_content"].ToString())
					{
						channel4.Online = 0;
						channel4.OnlineStr = "未在线";
					}
				}
				break;
			}
			case 116:
			{
				foreach (Channel channel5 in Channels)
				{
					if (channel5.Serial_number == msg_json_data["msg_content"].ToString())
					{
						channel5.Online = 1;
						channel5.OnlineStr = "在线";
					}
				}
				break;
			}
			case 117:
				if (acceptDataTimer != null)
				{
					acceptDataTimer.Stop();
				}
				TopStatusText = "升降牌打开,等待洗牌中...";
				msg_json_data["msg_content"].ToString().Split(new char[1] { '|' });
				break;
			}
		}
		else if (protocol.types == Constant.PROTOCAL_TYPE_S_FROM_SERVER_TYPE_OF_RESPONSE_KEEP_ALIVE)
		{
			Console.WriteLine("收到服务器的心跳包");
			DelayTime = (int)(DateTimeOffset.UtcNow.ToUnixTimeMilliseconds() - delayStartTime);
			if (DelayTime > 300)
			{
				DelayTimeError = true;
			}
			else
			{
				DelayTimeError = false;
			}
		}
		else if (protocol.types == Constant.ERROR_CODE_RESPONSE_FOR_UN_LOGIN)
		{
			Console.WriteLine("未登录");
			NetManager.Disconnect();
			NetManager.Init();
			NetManager.Connect(getWebStockLoginData());
		}
	}

	private void OnMessageThreadImg(Protocol protocol, string msgContent)
	{
		ProtocolDataContentImg protocolDataContentImg = JsonConvert.DeserializeObject<ProtocolDataContentImg>(msgContent);
		string text = AppDomain.CurrentDomain.BaseDirectory + "cacheImg\\";
		string text2 = protocolDataContentImg.serial_number + "_" + protocolDataContentImg.mahjong_number + "_" + Guid.NewGuid().ToString().Replace("-", "") + ".jpg";
		string fullPathFilename = text + text2;
		if (protocolDataContentImg.img == "")
		{
			return;
		}
		if (protocolDataContentImg.img != "x")
		{
			DownloadUrlImg(Global.FullImgUrl(protocolDataContentImg.img), text, text2);
		}
		foreach (Channel channel in channels)
		{
			if (!(channel.Serial_number == protocolDataContentImg.serial_number))
			{
				continue;
			}
			((DispatcherObject)Application.Current).Dispatcher.Invoke((Action)delegate
			{
				int num = protocolDataContentImg.mahjong_number - channel.DeleteOffset;
				if (num <= 0 || num > channel.Mj_count)
				{
					Console.WriteLine("丢弃越界WS消息: original=" + protocolDataContentImg.mahjong_number + ", offset=" + channel.DeleteOffset + ", effective=" + num);
				}
				else
				{
					int num2 = num / 2;
					if (num % 2 == 0)
					{
						num2--;
						channel.BottomMj[num2].MahjongNumber = protocolDataContentImg.mahjong_number;
						channel.BottomMj[num2].ResultImg = "/res/other/empty.jpg";
						channel.BottomMj[num2].UploadImg = "/res/other/empty.jpg";
						if (protocolDataContentImg.img != "x")
						{
							channel.BottomMj[num2].UploadImg = fullPathFilename;
							if (protocolDataContentImg.ai_val != -1)
							{
								MjConfig mjConfigByNumber = getMjConfigByNumber(protocolDataContentImg.ai_val);
								if (mjConfigByNumber != null)
								{
									channel.BottomMj[num2].ResultImg = mjConfigByNumber.Url;
									channel.BottomMj[num2].Result = protocolDataContentImg.ai_val.ToString();
									channel.BottomMj[num2].ResultJd = protocolDataContentImg.ai_precision;
								}
							}
							else
							{
								string[] array = shibieImg(fullPathFilename + "_180.jpg").Split(new char[1] { '$' });
								if (array.Length != 0)
								{
									string[] array2 = array[0].Split(new char[1] { '@' });
									MjConfig mjConfigByNumber2 = getMjConfigByNumber(int.Parse(array2[1]));
									if (mjConfigByNumber2 != null)
									{
										channel.BottomMj[num2].ResultImg = mjConfigByNumber2.Url;
										channel.BottomMj[num2].Result = array2[1];
										channel.BottomMj[num2].ResultJd = double.Parse(array2[0]);
									}
								}
								string[] array3 = shibieImg(fullPathFilename + "_180.jpg").Split(new char[1] { '$' });
								if (array3.Length != 0)
								{
									string[] array4 = array3[0].Split(new char[1] { '@' });
									MjConfig mjConfigByNumber3 = getMjConfigByNumber(int.Parse(array4[1]));
									if (mjConfigByNumber3 != null)
									{
										channel.BottomMj[num2].Result180Img = mjConfigByNumber3.Url;
										channel.BottomMj[num2].Reslut180 = array4[1];
										channel.BottomMj[num2].Result180Jd = double.Parse(array4[0]);
									}
									Console.WriteLine("BottomMj 180");
								}
								if (channel.BottomMj[num2].Result180Jd > channel.BottomMj[num2].ResultJd)
								{
									channel.BottomMj[num2].Result = channel.BottomMj[num2].Reslut180;
									channel.BottomMj[num2].ResultImg = channel.BottomMj[num2].Result180Img;
									Console.WriteLine("BottomMj 180 success!");
								}
							}
						}
						Console.WriteLine();
						Console.WriteLine("找到来牌方向Bottom");
						Console.WriteLine(channel.BottomMj[num2].UploadImg);
					}
					else
					{
						channel.TopMj[num2].MahjongNumber = protocolDataContentImg.mahjong_number;
						channel.TopMj[num2].ResultImg = "/res/other/empty.jpg";
						channel.TopMj[num2].UploadImg = "/res/other/empty.jpg";
						if (protocolDataContentImg.img != "x")
						{
							channel.TopMj[num2].UploadImg = fullPathFilename;
							if (protocolDataContentImg.ai_val != -1)
							{
								MjConfig mjConfigByNumber4 = getMjConfigByNumber(protocolDataContentImg.ai_val);
								if (mjConfigByNumber4 != null)
								{
									channel.TopMj[num2].ResultImg = mjConfigByNumber4.Url;
									channel.TopMj[num2].Result = protocolDataContentImg.ai_val.ToString();
									channel.TopMj[num2].ResultJd = protocolDataContentImg.ai_precision;
								}
							}
							else
							{
								channel.TopMj[num2].UploadImg = fullPathFilename;
								string[] array5 = shibieImg(fullPathFilename + "_180.jpg").Split(new char[1] { '$' });
								if (array5.Length != 0)
								{
									string[] array6 = array5[0].Split(new char[1] { '@' });
									MjConfig mjConfigByNumber5 = getMjConfigByNumber(int.Parse(array6[1]));
									if (mjConfigByNumber5 != null)
									{
										channel.TopMj[num2].ResultImg = mjConfigByNumber5.Url;
										channel.TopMj[num2].Result = array6[1];
										channel.TopMj[num2].ResultJd = double.Parse(array6[0]);
									}
									Console.WriteLine(array6[1]);
								}
								string[] array7 = shibieImg(fullPathFilename + "_180.jpg").Split(new char[1] { '$' });
								if (array7.Length != 0)
								{
									string[] array8 = array7[0].Split(new char[1] { '@' });
									MjConfig mjConfigByNumber6 = getMjConfigByNumber(int.Parse(array8[1]));
									if (mjConfigByNumber6 != null)
									{
										Console.WriteLine("TopMj 180 set channel val!");
										channel.TopMj[num2].Result180Img = mjConfigByNumber6.Url;
										channel.TopMj[num2].Reslut180 = array8[1];
										channel.TopMj[num2].Result180Jd = double.Parse(array8[0]);
									}
									Console.WriteLine("TopMj 180 resultJd:" + channel.TopMj[num2].ResultJd + ";180:" + channel.TopMj[num2].Result180Jd);
									Console.WriteLine("TOpMj 180 " + array7[0].ToString());
									Console.WriteLine("TopMj 180" + double.Parse(array8[0]));
								}
								if (channel.TopMj[num2].Result180Jd > channel.TopMj[num2].ResultJd)
								{
									channel.TopMj[num2].Result = channel.TopMj[num2].Reslut180;
									channel.TopMj[num2].ResultImg = channel.TopMj[num2].Result180Img;
									Console.WriteLine("TopMj 180 success!");
								}
							}
						}
						Console.WriteLine("找到来牌方向Top");
					}
					int num3 = 0;
					int num4 = 0;
					foreach (Channel channel2 in Channels)
					{
						foreach (ChannelMjResult item in channel2.TopMj)
						{
							if (item.Result != null && item.Result != "")
							{
								num3++;
							}
						}
						foreach (ChannelMjResult item2 in channel2.BottomMj)
						{
							if (item2.Result != null && item2.Result != "")
							{
								num3++;
							}
						}
						num4 += channel2.Mj_count;
					}
					if (upSend)
					{
						setUpMjData();
					}
					if (num3 == num4)
					{
						if (!upSend)
						{
							upSend = true;
							foreach (Channel upChannel in UpChannels)
							{
								foreach (ChannelMjResult item3 in upChannel.TopMj)
								{
									CleanMjItem(item3);
								}
								foreach (ChannelMjResult item4 in upChannel.BottomMj)
								{
									CleanMjItem(item4);
								}
							}
							setUpMjData();
						}
						((Selector)tabControl).SelectedIndex = 1;
						clacValidPageData();
						SendData(autoSend: true);
						Console.WriteLine("sumNUmber:" + num3);
						if (Wan1.Count() > 7 || Wan2.Count() > 7 || Wan3.Count() > 7 || Wan4.Count() > 7 || Wan5.Count() > 7 || Wan6.Count() > 7 || Wan7.Count() > 7 || Wan8.Count() > 7 || Wan9.Count() > 7 || Tiao1.Count() > 7 || Tiao2.Count() > 7 || Tiao3.Count() > 7 || Tiao4.Count() > 7 || Tiao5.Count() > 7 || Tiao6.Count() > 7 || Tiao7.Count() > 7 || Tiao8.Count() > 7 || Tiao9.Count() > 7 || Tong1.Count() > 7 || Tong2.Count() > 7 || Tong3.Count() > 7 || Tong4.Count() > 7 || Tong5.Count() > 7 || Tong6.Count() > 7 || Tong7.Count() > 7 || Tong8.Count() > 7 || Tong9.Count() > 7 || Dong.Count() > 7 || Lan.Count() > 7 || Xi.Count() > 7 || Bei.Count() > 7 || Zhong.Count() > 7 || Fa.Count() > 7 || Baipi.Count() > 7 || Baipi.Count() > 7)
						{
							using (StreamWriter streamWriter = new StreamWriter(".\\error_log.txt", append: true))
							{
								streamWriter.WriteLine(DateTime.Now.ToString() + "\n");
							}
						}
					}
					else
					{
						if (tabControl != null && ((Selector)tabControl).SelectedIndex == 1)
						{
							clacValidPageData();
						}
						PlayReceiveSound();
					}
				}
			});
		}
	}

	private void CleanMjItem(ChannelMjResult mj)
	{
		mj.Result = "";
		mj.ResultImg = "/res/top_bg.jpg";
		mj.Reslut180 = "";
		mj.Result180Img = "/res/top_bg.jpg";
		mj.ResultJd = 0.0;
		mj.Result180Jd = 0.0;
		mj.UploadImg = "/res/bottom_bg.jpg";
		mj.MahjongNumber = 0;
	}

	private ChannelMjResult GetLogicalSlot(Channel channel, int logicalIndex)
	{
		int index = logicalIndex / 2;
		if (logicalIndex % 2 == 0)
		{
			return channel.TopMj[index];
		}
		return channel.BottomMj[index];
	}

	private void CopyMjSlot(ChannelMjResult src, ChannelMjResult dst)
	{
		dst.MahjongNumber = src.MahjongNumber;
		dst.UploadImg = src.UploadImg;
		dst.Result = src.Result;
		dst.ResultImg = src.ResultImg;
		dst.ResultJd = src.ResultJd;
		dst.Reslut180 = src.Reslut180;
		dst.Result180Img = src.Result180Img;
		dst.Result180Jd = src.Result180Jd;
	}

	private void ShiftForward(Channel channel, int startLogicalIndex)
	{
		int num = channel.TopMj.Count * 2;
		for (int i = startLogicalIndex; i < num - 1; i++)
		{
			CopyMjSlot(GetLogicalSlot(channel, i + 1), GetLogicalSlot(channel, i));
		}
		CleanMjItem(GetLogicalSlot(channel, num - 1));
	}

	private Channel FindChannelByIndexPrefix(string dirKey)
	{
		foreach (Channel channel in channels)
		{
			if (channel.TopMj.Count > 0 && channel.TopMj[0].Index.StartsWith(dirKey + ":"))
			{
				return channel;
			}
		}
		return null;
	}

	public void setUpMjData()
	{
		foreach (Channel channel in channels)
		{
			foreach (Channel upChannel in UpChannels)
			{
				if (upChannel.Direction != channel.Direction)
				{
					continue;
				}
				for (int i = 0; i < channel.TopMj.Count; i++)
				{
					if (upChannel.TopMj[i].MahjongNumber != channel.TopMj[i].MahjongNumber)
					{
						upChannel.TopMj[i].MahjongNumber = channel.TopMj[i].MahjongNumber;
					}
					if (upChannel.TopMj[i].ResultImg != channel.TopMj[i].ResultImg)
					{
						upChannel.TopMj[i].ResultImg = channel.TopMj[i].ResultImg;
					}
					if (upChannel.TopMj[i].ResultImg != channel.TopMj[i].ResultImg)
					{
						upChannel.TopMj[i].ResultImg = channel.TopMj[i].ResultImg;
					}
					if (upChannel.TopMj[i].UploadImg != channel.TopMj[i].UploadImg)
					{
						upChannel.TopMj[i].UploadImg = channel.TopMj[i].UploadImg;
					}
					if (upChannel.TopMj[i].ResultImg != channel.TopMj[i].ResultImg)
					{
						upChannel.TopMj[i].ResultImg = channel.TopMj[i].ResultImg;
					}
					if (upChannel.TopMj[i].Result != channel.TopMj[i].Result)
					{
						upChannel.TopMj[i].Result = channel.TopMj[i].Result;
					}
				}
				for (int j = 0; j < channel.BottomMj.Count; j++)
				{
					if (upChannel.BottomMj[j].MahjongNumber != channel.BottomMj[j].MahjongNumber)
					{
						upChannel.BottomMj[j].MahjongNumber = channel.BottomMj[j].MahjongNumber;
					}
					if (upChannel.BottomMj[j].ResultImg != channel.BottomMj[j].ResultImg)
					{
						upChannel.BottomMj[j].ResultImg = channel.BottomMj[j].ResultImg;
					}
					if (upChannel.BottomMj[j].UploadImg != channel.BottomMj[j].UploadImg)
					{
						upChannel.BottomMj[j].UploadImg = channel.BottomMj[j].UploadImg;
					}
					if (upChannel.BottomMj[j].ResultImg != channel.BottomMj[j].ResultImg)
					{
						upChannel.BottomMj[j].ResultImg = channel.BottomMj[j].ResultImg;
					}
					if (upChannel.BottomMj[j].Result != channel.BottomMj[j].Result)
					{
						upChannel.BottomMj[j].Result = channel.BottomMj[j].Result;
					}
				}
			}
		}
	}

	public void setMjData()
	{
		foreach (Channel channel in channels)
		{
			foreach (Channel upChannel in UpChannels)
			{
				if (upChannel.Direction != channel.Direction)
				{
					continue;
				}
				for (int i = 0; i < channel.TopMj.Count; i++)
				{
					if (channel.TopMj[i].MahjongNumber != upChannel.TopMj[i].MahjongNumber)
					{
						channel.TopMj[i].MahjongNumber = upChannel.TopMj[i].MahjongNumber;
					}
					if (channel.TopMj[i].ResultImg != upChannel.TopMj[i].ResultImg)
					{
						channel.TopMj[i].ResultImg = upChannel.TopMj[i].ResultImg;
					}
					if (channel.TopMj[i].ResultImg != upChannel.TopMj[i].ResultImg)
					{
						channel.TopMj[i].ResultImg = upChannel.TopMj[i].ResultImg;
					}
					if (channel.TopMj[i].UploadImg != upChannel.TopMj[i].UploadImg)
					{
						channel.TopMj[i].UploadImg = upChannel.TopMj[i].UploadImg;
					}
					if (channel.TopMj[i].ResultImg != upChannel.TopMj[i].ResultImg)
					{
						channel.TopMj[i].ResultImg = upChannel.TopMj[i].ResultImg;
					}
					if (channel.TopMj[i].Result != upChannel.TopMj[i].Result)
					{
						channel.TopMj[i].Result = upChannel.TopMj[i].Result;
					}
				}
				for (int j = 0; j < channel.BottomMj.Count; j++)
				{
					if (channel.BottomMj[j].MahjongNumber != upChannel.BottomMj[j].MahjongNumber)
					{
						channel.BottomMj[j].MahjongNumber = upChannel.BottomMj[j].MahjongNumber;
					}
					if (channel.BottomMj[j].ResultImg != upChannel.BottomMj[j].ResultImg)
					{
						channel.BottomMj[j].ResultImg = upChannel.BottomMj[j].ResultImg;
					}
					if (channel.BottomMj[j].UploadImg != upChannel.BottomMj[j].UploadImg)
					{
						channel.BottomMj[j].UploadImg = upChannel.BottomMj[j].UploadImg;
					}
					if (channel.BottomMj[j].ResultImg != upChannel.BottomMj[j].ResultImg)
					{
						channel.BottomMj[j].ResultImg = upChannel.BottomMj[j].ResultImg;
					}
					if (channel.BottomMj[j].Result != upChannel.BottomMj[j].Result)
					{
						channel.BottomMj[j].Result = upChannel.BottomMj[j].Result;
					}
				}
			}
		}
	}

	private void startAcceptDataTimer()
	{
		if (acceptDataTimer == null)
		{
			timeout = Global.loginUser.Timeout;
			acceptDataTimer = new System.Timers.Timer();
			acceptDataTimer.Enabled = true;
			acceptDataTimer.Interval = 1000.0;
			acceptDataTimer.Start();
			acceptDataTimer.Elapsed += AcceptDataTimer_Elapsed;
		}
	}

	private void AcceptDataTimer_Elapsed(object sender, ElapsedEventArgs e)
	{
		timeout--;
		if (timeout <= 0)
		{
			acceptDataTimer.Stop();
			acceptDataTimer = null;
			TopStatusText = "等待接受数据";
		}
		else
		{
			TopStatusText = "接收数据中...（" + timeout + "）";
		}
	}

	private void startKeepaliveTimer()
	{
		if (keepaliveTimer == null)
		{
			keepaliveTimer = new System.Timers.Timer(1.0);
			keepaliveTimer.Enabled = true;
			keepaliveTimer.Start();
			keepaliveTimer.Elapsed += KeepaliveTimer_Elapsed;
		}
	}

	private void KeepaliveTimer_Elapsed(object sender, ElapsedEventArgs e)
	{
		if (keepaliveTimer.Interval == 1.0)
		{
			keepaliveTimer.Interval = 5000.0;
		}
		delayStartTime = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
		NetManager.Send(JsonConvert.SerializeObject((object)new Protocol
		{
			types = Constant.PROTOCOL_TYPE_C_FROM_CLIENT_TYPE_OF_KEEPALIVE,
			data_content = "",
			from = "",
			to = "",
			fp = Guid.NewGuid().ToString(),
			qos = false,
			retry_count = 0
		}));
	}

	private void startUpdateWsContent()
	{
		if (checkWsContentTimer == null)
		{
			checkWsContentTimer = new System.Timers.Timer(1.0);
			checkWsContentTimer.Enabled = true;
			checkWsContentTimer.Start();
			checkWsContentTimer.Elapsed += CheckWsContentTimer_Elapsed;
		}
	}

	private void CheckWsContentTimer_Elapsed(object sender, ElapsedEventArgs e)
	{
		if (checkWsContentTimer.Interval == 1.0)
		{
			checkWsContentTimer.Interval = 1000.0;
		}
		if (NetManager.IsConnect)
		{
			checkWsContentTimer.Interval = 1000.0;
			WsContentStatus = "链接成功";
			return;
		}
		WsContentStatus = "断开链接";
		checkWsContentTimer.Interval = 3000.0;
		NetManager.Disconnect();
		NetManager.Init();
		NetManager.Connect(getWebStockLoginData());
		WsContentStatus = "重连中...";
	}

	private void startCheckIsConnectInternetTimer()
	{
		if (checkIsConnectInternetTimer == null)
		{
			checkIsConnectInternetTimer = new System.Timers.Timer(1.0);
			checkIsConnectInternetTimer.Enabled = true;
			checkIsConnectInternetTimer.Start();
			checkIsConnectInternetTimer.Elapsed += CheckIsConnectInternetTimer_Elapsed;
		}
	}

	private void CheckIsConnectInternetTimer_Elapsed(object sender, ElapsedEventArgs e)
	{
		if (!IsConnectInternet.IsConnect())
		{
			NotConnectInternet = "  网络不可以，请检查您的网络设置   ";
		}
		else
		{
			NotConnectInternet = "";
		}
	}

	public void Logout()
	{
		deviceTimer?.Stop();
		deviceTimer = null;
		keepaliveTimer?.Stop();
		keepaliveTimer = null;
		webStockLogout();
		qosTimer.Stop();
		Global.RemoveCacheImg();
	}

	public void webStockLogout()
	{
		NetManager.Send(JsonConvert.SerializeObject((object)new Protocol
		{
			types = Constant.PROTOCOL_TYPE_C_FROM_CLIENT_TYPE_OF_LOGOUT,
			data_content = "",
			from = "",
			to = "",
			fp = Guid.NewGuid().ToString(),
			qos = false,
			retry_count = 0
		}));
	}

	public static string Base64ToImage(string strbase64, string path, string filename)
	{
		//IL_0013: Unknown result type (might be due to invalid IL or missing references)
		//IL_0057: Unknown result type (might be due to invalid IL or missing references)
		//IL_005c: Unknown result type (might be due to invalid IL or missing references)
		string result = "false";
		try
		{
			using MemoryStream memoryStream = new MemoryStream(Convert.FromBase64String(strbase64));
			Bitmap val = new Bitmap((Stream)memoryStream);
			if (!Directory.Exists(path))
			{
				Directory.CreateDirectory(path);
			}
			((Image)val).Save(path + "\\" + filename, ImageFormat.Jpeg);
			if (File.Exists(path + "\\" + filename))
			{
				result = "true";
			}
			Bitmap val2 = new Bitmap((Stream)memoryStream);
			((Image)val2).RotateFlip((RotateFlipType)2);
			((Image)val2).Save(path + "\\" + filename + "_180.jpg", ImageFormat.Jpeg);
			memoryStream.Close();
		}
		catch (Exception ex)
		{
			result = "error:" + ex.Message;
		}
		return result;
	}

	public void DownloadUrlImg(string url, string path, string filename)
	{
		if (!Directory.Exists(path))
		{
			Directory.CreateDirectory(path);
		}
		while (true)
		{
			try
			{
				Console.WriteLine("图片开始下载：" + url);
				Console.WriteLine(url);
				string text = path + filename + ".data";
				WebClient webClient = new WebClient();
				webClient.Encoding = Encoding.UTF8;
				webClient.DownloadFile(url, text);
				StreamReader streamReader = File.OpenText(text);
				Base64ToImage(Global.GetDataBash64(streamReader.ReadLine()), path, filename);
				Console.WriteLine("图片下载成功：" + url);
				streamReader.Close();
				File.Delete(text);
				break;
			}
			catch (Exception value)
			{
				Console.WriteLine(value);
			}
		}
	}

	public void RightChangeBut(string mjResult)
	{
		if (((Selector)tabControl).SelectedIndex == 1)
		{
			MjConfig mjConfigByNumber = getMjConfigByNumber(int.Parse(mjResult));
			if (mjConfigByNumber == null)
			{
				return;
			}
			foreach (Channel upChannel in UpChannels)
			{
				foreach (ChannelMjResult item in upChannel.TopMj)
				{
					if (item.ValidPageSelect)
					{
						item.Result = mjConfigByNumber.Number.ToString();
						item.ResultImg = mjConfigByNumber.Url;
						item.ValidPageSelect = false;
					}
				}
				foreach (ChannelMjResult item2 in upChannel.BottomMj)
				{
					if (item2.ValidPageSelect)
					{
						item2.Result = mjConfigByNumber.Number.ToString();
						item2.ResultImg = mjConfigByNumber.Url;
						item2.ValidPageSelect = false;
					}
				}
			}
			if (upSend)
			{
				setMjData();
			}
			clacValidPageData();
		}
		else
		{
			if (selectChannelMjResult == null)
			{
				return;
			}
			MjConfig mjConfig = getMjConfigByNumber(int.Parse(mjResult));
			if (mjConfig == null)
			{
				return;
			}
			((DispatcherObject)Application.Current).Dispatcher.Invoke((Action)delegate
			{
				selectChannelMjResult.Result = mjConfig.Number.ToString();
				selectChannelMjResult.ResultImg = mjConfig.Url;
				if (selectChannelMjResult.UploadImg == null || selectChannelMjResult.UploadImg.Length == 0)
				{
					selectChannelMjResult.UploadImg = mjConfig.Url;
				}
				if (upSend)
				{
					setUpMjData();
				}
			});
		}
	}

	private void startDeviceStatusTimer()
	{
		if (deviceTimer != null)
		{
			deviceTimer.Stop();
		}
		deviceTimer = new System.Timers.Timer();
		deviceTimer.Enabled = true;
		deviceTimer.Interval = 5000.0;
		deviceTimer.Start();
		deviceTimer.Elapsed += DeviceTimer_Elapsed;
	}

	private void DeviceTimer_Elapsed(object sender, ElapsedEventArgs e)
	{
		SelectAllDeviceStatus();
	}

	private void startCleanQosTimer()
	{
		qosTimer = new System.Timers.Timer();
		qosTimer.Enabled = true;
		qosTimer.Interval = 10000.0;
		qosTimer.Start();
		qosTimer.Elapsed += QosTimer_Elapsed;
	}

	private void QosTimer_Elapsed(object sender, ElapsedEventArgs e)
	{
		KeyValuePair<string, long>[] array = NetManager.qosList.ToArray();
		for (int i = 0; i < array.Length; i++)
		{
			KeyValuePair<string, long> keyValuePair = array[i];
			if (DateTimeOffset.UtcNow.ToUnixTimeMilliseconds() - keyValuePair.Value > 60000)
			{
				NetManager.qosList.Remove(keyValuePair.Key);
			}
		}
	}

	private string shibieImg(string imgPath)
	{
		Console.WriteLine(imgPath);
		string baseDirectory = AppDomain.CurrentDomain.BaseDirectory;
		return shibie(baseDirectory + "d.p", baseDirectory + "r.m", baseDirectory + "t.b", baseDirectory + "w.t", imgPath);
	}

	private void getMjServerData()
	{
		mjConfigList.Add(new MjConfig(1, 1, "一万", "/res/wan/1.jpg"));
		mjConfigList.Add(new MjConfig(2, 2, "二万", "/res/wan/2.jpg"));
		mjConfigList.Add(new MjConfig(3, 3, "三万", "/res/wan/3.jpg"));
		mjConfigList.Add(new MjConfig(4, 4, "四万", "/res/wan/4.jpg"));
		mjConfigList.Add(new MjConfig(5, 5, "五万", "/res/wan/5.jpg"));
		mjConfigList.Add(new MjConfig(6, 6, "六万", "/res/wan/6.jpg"));
		mjConfigList.Add(new MjConfig(7, 7, "七万", "/res/wan/7.jpg"));
		mjConfigList.Add(new MjConfig(8, 8, "八万", "/res/wan/8.jpg"));
		mjConfigList.Add(new MjConfig(9, 9, "九万", "/res/wan/9.jpg"));
		mjConfigList.Add(new MjConfig(11, 11, "一条", "/res/tiao/1.jpg"));
		mjConfigList.Add(new MjConfig(12, 12, "二条", "/res/tiao/2.jpg"));
		mjConfigList.Add(new MjConfig(13, 13, "三条", "/res/tiao/3.jpg"));
		mjConfigList.Add(new MjConfig(14, 14, "四条", "/res/tiao/4.jpg"));
		mjConfigList.Add(new MjConfig(15, 15, "五条", "/res/tiao/5.jpg"));
		mjConfigList.Add(new MjConfig(16, 16, "六条", "/res/tiao/6.jpg"));
		mjConfigList.Add(new MjConfig(17, 17, "七条", "/res/tiao/7.jpg"));
		mjConfigList.Add(new MjConfig(18, 18, "八条", "/res/tiao/8.jpg"));
		mjConfigList.Add(new MjConfig(19, 19, "九条", "/res/tiao/9.jpg"));
		mjConfigList.Add(new MjConfig(21, 21, "一筒", "/res/tong/1.jpg"));
		mjConfigList.Add(new MjConfig(22, 22, "二筒", "/res/tong/2.jpg"));
		mjConfigList.Add(new MjConfig(23, 23, "三筒", "/res/tong/3.jpg"));
		mjConfigList.Add(new MjConfig(24, 24, "四筒", "/res/tong/4.jpg"));
		mjConfigList.Add(new MjConfig(25, 25, "五筒", "/res/tong/5.jpg"));
		mjConfigList.Add(new MjConfig(26, 26, "六筒", "/res/tong/6.jpg"));
		mjConfigList.Add(new MjConfig(27, 27, "七筒", "/res/tong/7.jpg"));
		mjConfigList.Add(new MjConfig(28, 28, "八筒", "/res/tong/8.jpg"));
		mjConfigList.Add(new MjConfig(29, 29, "九筒", "/res/tong/9.jpg"));
		mjConfigList.Add(new MjConfig(31, 31, "东", "/res/sucai/zi/1.png"));
		mjConfigList.Add(new MjConfig(32, 32, "南", "/res/sucai/zi/2.png"));
		mjConfigList.Add(new MjConfig(33, 33, "西", "/res/sucai/zi/3.png"));
		mjConfigList.Add(new MjConfig(34, 34, "北", "/res/sucai/zi/4.png"));
		mjConfigList.Add(new MjConfig(35, 35, "中", "/res/sucai/zi/5.png"));
		mjConfigList.Add(new MjConfig(36, 36, "发", "/res/sucai/zi/6.png"));
		mjConfigList.Add(new MjConfig(37, 37, "北", "/res/sucai/zi/7.png"));
		mjConfigList.Add(new MjConfig(41, 41, "北", "/res/sucai/hua/1.png"));
		mjConfigList.Add(new MjConfig(42, 42, "北", "/res/sucai/hua/2.png"));
		mjConfigList.Add(new MjConfig(43, 43, "北", "/res/sucai/hua/3.png"));
		mjConfigList.Add(new MjConfig(44, 44, "北", "/res/sucai/hua/4.png"));
		mjConfigList.Add(new MjConfig(45, 45, "北", "/res/sucai/hua/5.png"));
		mjConfigList.Add(new MjConfig(46, 46, "北", "/res/sucai/hua/6.png"));
		mjConfigList.Add(new MjConfig(47, 47, "北", "/res/sucai/hua/7.png"));
		mjConfigList.Add(new MjConfig(48, 48, "北", "/res/sucai/hua/8.png"));
	}

	private MjConfig getMjConfigByNumber(int number)
	{
		foreach (MjConfig mjConfig in mjConfigList)
		{
			if (mjConfig.Number == number)
			{
				return mjConfig;
			}
		}
		return null;
	}

	public bool startGames()
	{
		//IL_0010: Unknown result type (might be due to invalid IL or missing references)
		//IL_0015: Unknown result type (might be due to invalid IL or missing references)
		//IL_001b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0025: Expected O, but got Unknown
		//IL_006e: Unknown result type (might be due to invalid IL or missing references)
		//IL_004e: Unknown result type (might be due to invalid IL or missing references)
		string text = HttpReq.get("zhuo/start_games");
		Dictionary<string, object> dictionary = new Dictionary<string, object>();
		JsonSerializer val = new JsonSerializer();
		((Collection<JsonConverter>)(object)val.Converters).Add((JsonConverter)new JavaScriptDateTimeConverter());
		val.NullValueHandling = (NullValueHandling)1;
		dictionary = JsonConvert.DeserializeObject<Dictionary<string, object>>(text);
		if ((bool)dictionary["success"])
		{
			MessageBox.Show("开始游戏成功", "消息提示", (MessageBoxButton)0);
			return true;
		}
		MessageBox.Show(dictionary["message"].ToString(), "错误提示", (MessageBoxButton)0, (MessageBoxImage)16);
		return false;
	}

	public bool endGames()
	{
		//IL_0010: Unknown result type (might be due to invalid IL or missing references)
		//IL_0015: Unknown result type (might be due to invalid IL or missing references)
		//IL_001b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0025: Expected O, but got Unknown
		//IL_006e: Unknown result type (might be due to invalid IL or missing references)
		//IL_004e: Unknown result type (might be due to invalid IL or missing references)
		string text = HttpReq.get("zhuo/end_games");
		Dictionary<string, object> dictionary = new Dictionary<string, object>();
		JsonSerializer val = new JsonSerializer();
		((Collection<JsonConverter>)(object)val.Converters).Add((JsonConverter)new JavaScriptDateTimeConverter());
		val.NullValueHandling = (NullValueHandling)1;
		dictionary = JsonConvert.DeserializeObject<Dictionary<string, object>>(text);
		if ((bool)dictionary["success"])
		{
			MessageBox.Show("结束游戏成功", "消息提示", (MessageBoxButton)0);
			return true;
		}
		MessageBox.Show(dictionary["message"].ToString(), "错误提示", (MessageBoxButton)0, (MessageBoxImage)16);
		return false;
	}

	public void CompareData(ObservableCollection<ChannelMjResult> oldList, ObservableCollection<ChannelMjResult> newList)
	{
		foreach (ChannelMjResult @new in newList)
		{
			if (!oldList.Contains(@new))
			{
				oldList.Add(@new);
			}
		}
		ChannelMjResult[] array = oldList.ToArray();
		foreach (ChannelMjResult item in array)
		{
			if (!newList.Contains(item))
			{
				oldList.Remove(item);
			}
		}
	}

	public void clacValidPageData()
	{
		Console.WriteLine("start");
		CompareData(Wan1, FullMj("1"));
		CompareData(Wan2, FullMj("2"));
		CompareData(Wan3, FullMj("3"));
		CompareData(Wan4, FullMj("4"));
		CompareData(Wan5, FullMj("5"));
		CompareData(Wan6, FullMj("6"));
		CompareData(Wan7, FullMj("7"));
		CompareData(Wan8, FullMj("8"));
		CompareData(Wan9, FullMj("9"));
		Wan1StatusNumber = Wan1.Count();
		Wan2StatusNumber = Wan2.Count();
		Wan3StatusNumber = Wan3.Count();
		Wan4StatusNumber = Wan4.Count();
		Wan5StatusNumber = Wan5.Count();
		Wan6StatusNumber = Wan6.Count();
		Wan7StatusNumber = Wan7.Count();
		Wan8StatusNumber = Wan8.Count();
		Wan9StatusNumber = Wan9.Count();
		CompareData(Tiao1, FullMj("11"));
		CompareData(Tiao2, FullMj("12"));
		CompareData(Tiao3, FullMj("13"));
		CompareData(Tiao4, FullMj("14"));
		CompareData(Tiao5, FullMj("15"));
		CompareData(Tiao6, FullMj("16"));
		CompareData(Tiao7, FullMj("17"));
		CompareData(Tiao8, FullMj("18"));
		CompareData(Tiao9, FullMj("19"));
		Tiao1StatusNumber = Tiao1.Count();
		Tiao2StatusNumber = Tiao2.Count();
		Tiao3StatusNumber = Tiao3.Count();
		Tiao4StatusNumber = Tiao4.Count();
		Tiao5StatusNumber = Tiao5.Count();
		Tiao6StatusNumber = Tiao6.Count();
		Tiao7StatusNumber = Tiao7.Count();
		Tiao8StatusNumber = Tiao8.Count();
		Tiao9StatusNumber = Tiao9.Count();
		CompareData(Tong1, FullMj("21"));
		CompareData(Tong2, FullMj("22"));
		CompareData(Tong3, FullMj("23"));
		CompareData(Tong4, FullMj("24"));
		CompareData(Tong5, FullMj("25"));
		CompareData(Tong6, FullMj("26"));
		CompareData(Tong7, FullMj("27"));
		CompareData(Tong8, FullMj("28"));
		CompareData(Tong9, FullMj("29"));
		Tong1StatusNumber = Tong1.Count();
		Tong2StatusNumber = Tong2.Count();
		Tong3StatusNumber = Tong3.Count();
		Tong4StatusNumber = Tong4.Count();
		Tong5StatusNumber = Tong5.Count();
		Tong6StatusNumber = Tong6.Count();
		Tong7StatusNumber = Tong7.Count();
		Tong8StatusNumber = Tong8.Count();
		Tong9StatusNumber = Tong9.Count();
		CompareData(DongFang, FullMj("31"));
		CompareData(Nan, FullMj("32"));
		CompareData(Xi, FullMj("33"));
		CompareData(Bei, FullMj("34"));
		CompareData(Zhong, FullMj("35"));
		CompareData(Fa, FullMj("36"));
		CompareData(Bai, FullMj("37"));
		DongFangStatusNumber = DongFang.Count();
		NanStatusNumber = Nan.Count();
		XiStatusNumber = Xi.Count();
		BeiStatusNumber = Bei.Count();
		ZhongStatusNumber = Zhong.Count();
		FaStatusNumber = Fa.Count();
		BaiStatusNumber = Bai.Count();
		CompareData(Chun, FullMj("41"));
		CompareData(Xia, FullMj("42"));
		CompareData(Qiu, FullMj("43"));
		CompareData(Dong, FullMj("44"));
		CompareData(Mei, FullMj("45"));
		CompareData(Lan, FullMj("46"));
		CompareData(Ju, FullMj("47"));
		CompareData(Zhu, FullMj("48"));
		CompareData(X, FullMj("X"));
		Console.WriteLine("end");
	}

	public ObservableCollection<ChannelMjResult> FullMj(string result)
	{
		ObservableCollection<ChannelMjResult> observableCollection = new ObservableCollection<ChannelMjResult>();
		foreach (ChannelMjResult channelMjResult in GetChannelMjResults(result))
		{
			observableCollection.Add(channelMjResult);
		}
		return observableCollection;
	}

	public List<ChannelMjResult> GetChannelMjResults(string result)
	{
		List<ChannelMjResult> list = new List<ChannelMjResult>();
		foreach (Channel upChannel in UpChannels)
		{
			foreach (ChannelMjResult item in upChannel.TopMj)
			{
				if (item.Result == result)
				{
					list.Add(item);
				}
			}
			foreach (ChannelMjResult item2 in upChannel.BottomMj)
			{
				if (item2.Result == result)
				{
					list.Add(item2);
				}
			}
		}
		return list;
	}

	public void RestartGames()
	{
		//IL_0010: Unknown result type (might be due to invalid IL or missing references)
		//IL_0015: Unknown result type (might be due to invalid IL or missing references)
		//IL_001b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0025: Expected O, but got Unknown
		//IL_0046: Unknown result type (might be due to invalid IL or missing references)
		string text = HttpReq.get("zhuo/zhuo_reset_games");
		new Dictionary<string, object>();
		JsonSerializer val = new JsonSerializer();
		((Collection<JsonConverter>)(object)val.Converters).Add((JsonConverter)new JavaScriptDateTimeConverter());
		val.NullValueHandling = (NullValueHandling)1;
		if ((bool)JsonConvert.DeserializeObject<Dictionary<string, object>>(text)["success"])
		{
			MessageBox.Show("重新开始游戏成功!");
		}
		CleanMj(channels);
	}

	public void CleanMj(ObservableCollection<Channel> channelMjs)
	{
		foreach (Channel channelMj in channelMjs)
		{
			foreach (ChannelMjResult item in channelMj.TopMj)
			{
				CleanMjItem(item);
			}
			foreach (ChannelMjResult item2 in channelMj.BottomMj)
			{
				CleanMjItem(item2);
			}
		}
		clacValidPageData();
	}

	public void SelectAllDeviceStatus()
	{
		//IL_001c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0021: Unknown result type (might be due to invalid IL or missing references)
		//IL_0027: Unknown result type (might be due to invalid IL or missing references)
		//IL_0031: Expected O, but got Unknown
		string text = HttpReq.get("zhuo/device_status_all");
		if (Ext.IsNullOrEmpty(text))
		{
			return;
		}
		Dictionary<string, object> dictionary = new Dictionary<string, object>();
		JsonSerializer val = new JsonSerializer();
		((Collection<JsonConverter>)(object)val.Converters).Add((JsonConverter)new JavaScriptDateTimeConverter());
		val.NullValueHandling = (NullValueHandling)1;
		dictionary = JsonConvert.DeserializeObject<Dictionary<string, object>>(text);
		if (!(bool)dictionary["success"])
		{
			return;
		}
		List<Dictionary<string, object>> list = JsonConvert.DeserializeObject<List<Dictionary<string, object>>>(dictionary["data"].ToString());
		foreach (Channel channel in channels)
		{
			foreach (Dictionary<string, object> item in list)
			{
				if (channel.Serial_number == item["serial_no"].ToString())
				{
					channel.Ip = item["ip"].ToString();
					channel.Online = int.Parse(item["online"].ToString());
					channel.OnlineStr = ((channel.Online == 1) ? "在线" : "未在线");
				}
			}
		}
	}

	public void SelectDeviceStatus(int deviceId)
	{
		//IL_0028: Unknown result type (might be due to invalid IL or missing references)
		//IL_002d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0033: Unknown result type (might be due to invalid IL or missing references)
		//IL_003d: Expected O, but got Unknown
		string text = HttpReq.get("zhuo/device_status/" + deviceId);
		if (Ext.IsNullOrEmpty(text))
		{
			return;
		}
		Dictionary<string, object> dictionary = new Dictionary<string, object>();
		JsonSerializer val = new JsonSerializer();
		((Collection<JsonConverter>)(object)val.Converters).Add((JsonConverter)new JavaScriptDateTimeConverter());
		val.NullValueHandling = (NullValueHandling)1;
		dictionary = JsonConvert.DeserializeObject<Dictionary<string, object>>(text);
		if (!(bool)dictionary["success"])
		{
			return;
		}
		Dictionary<string, object> dictionary2 = JsonConvert.DeserializeObject<Dictionary<string, object>>(dictionary["data"].ToString());
		foreach (Channel channel in channels)
		{
			if (channel.Id == deviceId)
			{
				channel.Ip = dictionary2["ip"].ToString();
				channel.Online = int.Parse(dictionary2["online"].ToString());
				channel.OnlineStr = ((channel.Online == 1) ? "在线" : "未在线");
			}
		}
	}

	public void SendData(bool autoSend)
	{
		new Thread((ThreadStart)delegate
		{
			//IL_01f4: Unknown result type (might be due to invalid IL or missing references)
			//IL_01f9: Unknown result type (might be due to invalid IL or missing references)
			//IL_01ff: Unknown result type (might be due to invalid IL or missing references)
			//IL_0209: Expected O, but got Unknown
			SendDataLoading = (Visibility)0;
			try
			{
				if (!autoSend || SendVlidateIsCheck)
				{
					bool flag = OnValidData();
					if (!SendVlidateIsCheck)
					{
						flag = true;
					}
					if (flag)
					{
						Console.WriteLine("发送数据");
						List<SaveMj> saveList = new List<SaveMj>();
						foreach (Channel upChannel in UpChannels)
						{
							foreach (ChannelMjResult item in upChannel.TopMj)
							{
								if (item.UploadImg != null && item.UploadImg != "/res/bottom_bg.jpg" && item.Result != "")
								{
									saveList.Add(new SaveMj(item.MahjongNumber, int.Parse(upChannel.Serial_number), upChannel.Direction, int.Parse(item.Result)));
								}
							}
							foreach (ChannelMjResult item2 in upChannel.BottomMj)
							{
								if (item2.UploadImg != null && item2.UploadImg != "/res/bottom_bg.jpg" && item2.Result != "")
								{
									saveList.Add(new SaveMj(item2.MahjongNumber, int.Parse(upChannel.Serial_number), upChannel.Direction, int.Parse(item2.Result)));
								}
							}
						}
						string jsonData = JsonConvert.SerializeObject((object)saveList);
						string text = HttpReq.post("zhuo/add", jsonData);
						Dictionary<string, object> respObj = new Dictionary<string, object>();
						JsonSerializer val = new JsonSerializer();
						((Collection<JsonConverter>)(object)val.Converters).Add((JsonConverter)new JavaScriptDateTimeConverter());
						val.NullValueHandling = (NullValueHandling)1;
						respObj = JsonConvert.DeserializeObject<Dictionary<string, object>>(text);
						((DispatcherObject)Application.Current).Dispatcher.Invoke((Action)delegate
						{
							//IL_011f: Unknown result type (might be due to invalid IL or missing references)
							if ((bool)respObj["success"])
							{
								PlayNotError();
								sendSuccessFullSendLog(autoSend, saveList.Count);
								if (upSend)
								{
									CleanMj(channels);
									CleanMj(upChannels);
								}
								foreach (Channel channel in channels)
								{
									channel.DeleteOffset = 0;
								}
								((Selector)tabControl).SelectedIndex = 0;
								upSend = true;
							}
							else
							{
								MessageBox.Show(respObj["msg"].ToString(), "错误提示", (MessageBoxButton)0, (MessageBoxImage)16);
							}
						});
						SendDataLoading = (Visibility)1;
					}
					else
					{
						PlayError();
						SendDataLoading = (Visibility)1;
					}
				}
			}
			catch (Exception)
			{
				SendDataLoading = (Visibility)1;
			}
		}).Start();
	}

	private bool OnValidData()
	{
		int num = 0;
		int num2 = 0;
		bool result = true;
		Dictionary<string, int> dictionary = new Dictionary<string, int>();
		foreach (Channel upChannel in UpChannels)
		{
			int num3 = 0;
			foreach (ChannelMjResult item in upChannel.TopMj)
			{
				if (item.UploadImg != null && item.UploadImg != "" && item.Result != null && item.Result != "")
				{
					if (!dictionary.ContainsKey(item.Result))
					{
						dictionary.Add(item.Result, 0);
					}
					int num4 = dictionary[item.Result];
					dictionary[item.Result] = num4 + 1;
					num2++;
					num3++;
				}
			}
			foreach (ChannelMjResult item2 in upChannel.BottomMj)
			{
				if (item2.UploadImg != null && item2.UploadImg != "" && item2.Result != null && item2.Result != "")
				{
					if (!dictionary.ContainsKey(item2.Result))
					{
						dictionary.Add(item2.Result, 0);
					}
					int num5 = dictionary[item2.Result];
					dictionary[item2.Result] = num5 + 1;
					num2++;
					num3++;
				}
			}
			num += upChannel.Mj_count;
			if (num3 != upChannel.Mj_count)
			{
				Console.WriteLine("错误" + upChannel.Direction + "配置为" + upChannel.Mj_count + "张，现在" + num3 + "张");
				result = false;
			}
		}
		foreach (KeyValuePair<string, int> item3 in dictionary)
		{
			if (int.Parse(item3.Key) < 40 && item3.Value != 4)
			{
				Console.WriteLine("错误" + item3.Key + ",数量应该为4，现在数量为" + item3.Value);
				result = false;
			}
		}
		if (num2 != num)
		{
			Console.WriteLine("配置合计为" + num + ",目前为：" + num2 + "张");
			result = false;
		}
		return result;
	}

	public void PlayNotError()
	{
		//IL_0005: Unknown result type (might be due to invalid IL or missing references)
		new SoundPlayer("./res/not_error.wav").Play();
	}

	public void PlayError()
	{
		//IL_0005: Unknown result type (might be due to invalid IL or missing references)
		new SoundPlayer("./res/error.wav").Play();
	}

	public void PlayReceiveSound()
	{
		//IL_0005: Unknown result type (might be due to invalid IL or missing references)
		new SoundPlayer("./res/receive_sound.wav").Play();
	}

	private void ExecutValidMj(ChannelMjResult channelMjResult)
	{
		channelMjResult.ValidPageSelect = !channelMjResult.ValidPageSelect;
	}

	internal void RemoveItem()
	{
		if (selectChannelMjResult == null)
		{
			return;
		}
		string[] array = selectChannelMjResult.Index.Split(new char[1] { ':' });
		if (array.Length < 3)
		{
			return;
		}
		string dirKey = array[0];
		string text = array[1];
		if (!int.TryParse(array[2], out var result))
		{
			return;
		}
		int num = ((text == "top") ? (result * 2) : (result * 2 + 1));
		Channel channel = FindChannelByIndexPrefix(dirKey);
		if (channel == null)
		{
			return;
		}
		int num2 = channel.TopMj.Count * 2;
		if (num >= 0 && num < num2)
		{
			Console.WriteLine("删除并前移: " + selectChannelMjResult.Index + ", 逻辑位置: " + num);
			ShiftForward(channel, num);
			channel.DeleteOffset++;
			if (upSend)
			{
				setUpMjData();
			}
			if (tabControl != null && ((Selector)tabControl).SelectedIndex == 1)
			{
				clacValidPageData();
			}
		}
	}

	public bool checkAppVersion()
	{
		//IL_0024: Unknown result type (might be due to invalid IL or missing references)
		//IL_0029: Unknown result type (might be due to invalid IL or missing references)
		//IL_002f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0039: Expected O, but got Unknown
		//IL_0065: Unknown result type (might be due to invalid IL or missing references)
		//IL_015f: Unknown result type (might be due to invalid IL or missing references)
		//IL_013c: Unknown result type (might be due to invalid IL or missing references)
		string text = HttpReq.get("app/check_update?type=3&version=" + Global.app_version + "&app_name=" + Global.app_name);
		Dictionary<string, object> dictionary = new Dictionary<string, object>();
		JsonSerializer val = new JsonSerializer();
		((Collection<JsonConverter>)(object)val.Converters).Add((JsonConverter)new JavaScriptDateTimeConverter());
		val.NullValueHandling = (NullValueHandling)1;
		dictionary = JsonConvert.DeserializeObject<Dictionary<string, object>>(text);
		if ((bool)dictionary["success"])
		{
			MessageBox.Show("有新版本发布、请立即下载新版本客户端", "升级提示", (MessageBoxButton)0);
			AppVersion appVersion = JsonConvert.DeserializeObject<AppVersion>(JsonConvert.SerializeObject(dictionary["data"]));
			string currentDirectory = Directory.GetCurrentDirectory();
			string text2 = "";
			foreach (KeyValuePair<string, string> item in appVersion.Ota_list)
			{
				string text3 = currentDirectory + "/" + item.Key + ".old";
				string text4 = currentDirectory + "/" + item.Key;
				if (File.Exists(text3))
				{
					File.Delete(text3);
				}
				File.Move(text4, text3);
				try
				{
					new WebClient().DownloadFile(item.Value, text4);
				}
				catch (Exception ex)
				{
					File.Move(text3, text4);
					text2 = ex.Message;
				}
			}
			if (text2 != "")
			{
				MessageBox.Show("自动升级失败、请手动下载更新！");
				Process.Start(appVersion.Update_url);
				Application.Current.Shutdown();
			}
			else
			{
				MessageBox.Show("升级成功");
				Assembly.GetEntryAssembly();
				Process.Start(Directory.GetCurrentDirectory() + "\\distinguish.exe");
				Application.Current.Shutdown();
			}
			return true;
		}
		return false;
	}

	private void fullLastSendTime(bool autoSend)
	{
		string text = "上次发送时间：";
		string text2 = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
		text += text2;
		text = ((!autoSend) ? (text + "（手动）") : (text + "（自动）"));
		LastSendTimeStr = text;
	}

	private void sendSuccessFullSendLog(bool autoSend, int sumMjNumber)
	{
		fullLastSendTime(autoSend);
		SendSumNumber++;
		if (autoSend)
		{
			AutoSendNumber++;
		}
		else
		{
			ManuallySendNumber++;
		}
		SendDataLog sendDataLog = new SendDataLog();
		sendDataLog.AutoSend = autoSend;
		sendDataLog.SendNumber = sumMjNumber;
		sendDataLog.SendTimeStr = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
		sendDataLog.SeneDateTime = default(DateTime);
		SendDataLogList.Add(sendDataLog);
		SendDataTotalStr = "合计发送：" + SendSumNumber + "次，自动：" + AutoSendNumber + "次，手动：" + ManuallySendNumber + "次。";
	}
}
