using System;
using System.CodeDom.Compiler;
using System.ComponentModel;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Markup;
using distinguish.Common;
using distinguish.Model;
using distinguish.ViewModel;

namespace distinguish;

public partial class HomeWindow : Window, IComponentConnector
{
	internal Grid MainGrid;

	internal TabControl MainTabControl;

	public HomeWindow(LoginResp loginResp, string ipAddr)
	{
		//IL_001f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0029: Expected Obj, but got Unknown
		Application.Current.MainWindow = (Window)(object)this;
		InitializeComponent();
		((FrameworkElement)this).SizeChanged += HomeWindow_SizeChanged;
		((Window)this).Closing += HomeWindow_Closing;
		(Global.HomeViewModel = (HomeViewModel)(((FrameworkElement)this).DataContext = new HomeViewModel(loginResp, ipAddr))).tabControl = MainTabControl;
	}

	private void HomeWindow_Closing(object sender, CancelEventArgs e)
	{
		(((FrameworkElement)this).DataContext as HomeViewModel).Logout();
	}

	private void HomeWindow_SizeChanged(object sender, SizeChangedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		Size newSize = e.NewSize;
		Console.WriteLine(newSize.Height);
		Console.WriteLine(((FrameworkElement)this).ActualHeight);
		HomeViewModel obj = ((FrameworkElement)this).DataContext as HomeViewModel;
		obj.ChannelHeight = (((FrameworkElement)this).ActualHeight - 92.0) / 4.0 - 1.0;
		obj.ChannelMjHeight = (obj.ChannelHeight - 25.0) / 2.0 / 2.0;
		obj.ChannelMjWidth = obj.ChannelMjHeight + 15.0;
		Console.Write(obj.ChannelMjHeight);
	}

	private void Online_Click(object sender, RoutedEventArgs e)
	{
		((Window)new OnlineWindows()).ShowDialog();
	}

	private void Preview_Click(object sender, RoutedEventArgs e)
	{
		HomeViewModel homeViewModel = ((FrameworkElement)this).DataContext as HomeViewModel;
		((Window)new PreviewWindow(homeViewModel.IpAddr, homeViewModel.PreviewSettingViewModel)).ShowDialog();
	}

	private void StartGames_Click(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as HomeViewModel).startGames();
	}

	private void EndGames_Click(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as HomeViewModel).endGames();
	}

	private void TabControl_SelectionChanged(object sender, SelectionChangedEventArgs e)
	{
		HomeViewModel homeViewModel = ((FrameworkElement)this).DataContext as HomeViewModel;
		if (((Selector)MainTabControl).SelectedIndex != 0 && ((Selector)MainTabControl).SelectedIndex == 1)
		{
			homeViewModel.clacValidPageData();
		}
	}

	private void RestartGames_Click(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as HomeViewModel).RestartGames();
	}

	private void ValidationSendData_Click(object sender, RoutedEventArgs e)
	{
		Console.WriteLine("ddd");
		HomeViewModel homeViewModel = ((FrameworkElement)this).DataContext as HomeViewModel;
		if (((Selector)MainTabControl).SelectedIndex == 0)
		{
			((Selector)MainTabControl).SelectedIndex = 1;
			homeViewModel.clacValidPageData();
		}
		else if (((Selector)MainTabControl).SelectedIndex == 1)
		{
			((Selector)MainTabControl).SelectedIndex = 0;
		}
	}

	private void SendData_Click(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as HomeViewModel).SendData(autoSend: false);
	}

	private void DongReload_Click(object sender, RoutedEventArgs e)
	{
		SendSelectDeviceStatusReq(1);
	}

	private void NanReload_Click(object sender, RoutedEventArgs e)
	{
		SendSelectDeviceStatusReq(2);
	}

	private void XiReload_Click(object sender, RoutedEventArgs e)
	{
		SendSelectDeviceStatusReq(3);
	}

	private void BeiReload_Click(object sender, RoutedEventArgs e)
	{
		SendSelectDeviceStatusReq(4);
	}

	private void SendSelectDeviceStatusReq(int dir)
	{
		HomeViewModel homeViewModel = ((FrameworkElement)this).DataContext as HomeViewModel;
		foreach (Channel channel in homeViewModel.Channels)
		{
			Console.WriteLine($"{channel.Direction}");
			if (channel.Direction == dir)
			{
				homeViewModel.SelectDeviceStatus(channel.Id);
				break;
			}
		}
	}

	private void Logout_Click(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as HomeViewModel).Logout();
		((Window)new MainWindow()).Show();
		((Window)this).Close();
	}

	private void EditPwd_Click(object sender, RoutedEventArgs e)
	{
		((Window)new EditPwdWindow()).ShowDialog();
	}

	private void RemoveItem_Click(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as HomeViewModel).RemoveItem();
	}

	[DebuggerNonUserCode]
	[GeneratedCode("PresentationBuildTasks", "4.0.0.0")]
	[EditorBrowsable(EditorBrowsableState.Never)]
	void IComponentConnector.Connect(int connectionId, object target)
	{
		//IL_0052: Unknown result type (might be due to invalid IL or missing references)
		//IL_005e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0068: Expected Obj, but got Unknown
		//IL_006b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0075: Expected Obj, but got Unknown
		//IL_0077: Unknown result type (might be due to invalid IL or missing references)
		//IL_0083: Unknown result type (might be due to invalid IL or missing references)
		//IL_008d: Expected Obj, but got Unknown
		//IL_008f: Unknown result type (might be due to invalid IL or missing references)
		//IL_009b: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a5: Expected Obj, but got Unknown
		//IL_00a7: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b3: Unknown result type (might be due to invalid IL or missing references)
		//IL_00bd: Expected Obj, but got Unknown
		//IL_00bf: Unknown result type (might be due to invalid IL or missing references)
		//IL_00cb: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d5: Expected Obj, but got Unknown
		//IL_00d7: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e3: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ed: Expected Obj, but got Unknown
		//IL_00ef: Unknown result type (might be due to invalid IL or missing references)
		//IL_00fb: Unknown result type (might be due to invalid IL or missing references)
		//IL_0105: Expected Obj, but got Unknown
		//IL_0107: Unknown result type (might be due to invalid IL or missing references)
		//IL_0113: Unknown result type (might be due to invalid IL or missing references)
		//IL_011d: Expected Obj, but got Unknown
		//IL_011f: Unknown result type (might be due to invalid IL or missing references)
		//IL_012b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0135: Expected Obj, but got Unknown
		//IL_0137: Unknown result type (might be due to invalid IL or missing references)
		//IL_0143: Unknown result type (might be due to invalid IL or missing references)
		//IL_014d: Expected Obj, but got Unknown
		//IL_0150: Unknown result type (might be due to invalid IL or missing references)
		//IL_015a: Expected Obj, but got Unknown
		//IL_0167: Unknown result type (might be due to invalid IL or missing references)
		//IL_0171: Expected Obj, but got Unknown
		//IL_0173: Unknown result type (might be due to invalid IL or missing references)
		//IL_017f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0189: Expected Obj, but got Unknown
		//IL_018b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0197: Unknown result type (might be due to invalid IL or missing references)
		//IL_01a1: Expected Obj, but got Unknown
		//IL_01a3: Unknown result type (might be due to invalid IL or missing references)
		//IL_01af: Unknown result type (might be due to invalid IL or missing references)
		//IL_01b9: Expected Obj, but got Unknown
		//IL_01bb: Unknown result type (might be due to invalid IL or missing references)
		//IL_01c7: Unknown result type (might be due to invalid IL or missing references)
		//IL_01d1: Expected Obj, but got Unknown
		//IL_01d3: Unknown result type (might be due to invalid IL or missing references)
		//IL_01df: Unknown result type (might be due to invalid IL or missing references)
		//IL_01e9: Expected Obj, but got Unknown
		switch (connectionId)
		{
		case 1:
			((MenuItem)target).Click += RemoveItem_Click;
			break;
		case 2:
			MainGrid = (Grid)target;
			break;
		case 3:
			((ButtonBase)(Button)target).Click += Online_Click;
			break;
		case 4:
			((ButtonBase)(Button)target).Click += Preview_Click;
			break;
		case 5:
			((ButtonBase)(Button)target).Click += StartGames_Click;
			break;
		case 6:
			((ButtonBase)(Button)target).Click += EndGames_Click;
			break;
		case 7:
			((ButtonBase)(Button)target).Click += RestartGames_Click;
			break;
		case 8:
			((ButtonBase)(Button)target).Click += ValidationSendData_Click;
			break;
		case 9:
			((ButtonBase)(Button)target).Click += SendData_Click;
			break;
		case 10:
			((ButtonBase)(Button)target).Click += EditPwd_Click;
			break;
		case 11:
			((ButtonBase)(Button)target).Click += Logout_Click;
			break;
		case 12:
			MainTabControl = (TabControl)target;
			((Selector)MainTabControl).SelectionChanged += TabControl_SelectionChanged;
			break;
		case 13:
			((ButtonBase)(Button)target).Click += DongReload_Click;
			break;
		case 14:
			((ButtonBase)(Button)target).Click += NanReload_Click;
			break;
		case 15:
			((ButtonBase)(Button)target).Click += XiReload_Click;
			break;
		case 16:
			((ButtonBase)(Button)target).Click += BeiReload_Click;
			break;
		case 17:
			((ButtonBase)(Button)target).Click += SendData_Click;
			break;
		default:
			_contentLoaded = true;
			break;
		}
	}
}
