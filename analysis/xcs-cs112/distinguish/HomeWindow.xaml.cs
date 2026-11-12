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
}
