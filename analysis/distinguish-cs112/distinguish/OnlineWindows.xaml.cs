using System.CodeDom.Compiler;
using System.ComponentModel;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Markup;
using distinguish.ViewModel;

namespace distinguish;

public partial class OnlineWindows : Window, IComponentConnector
{
	public OnlineWindows()
	{
		((FrameworkElement)this).DataContext = new OnlineConfigViewModel();
		InitializeComponent();
	}

	private void SaveConfigNumber_Click(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as OnlineConfigViewModel).saveConfigNumber();
	}

	private void SaveDataConfig_Click(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as OnlineConfigViewModel).saveDatgaConfig();
	}

	private void SaveWifi_Click(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as OnlineConfigViewModel).saveWifiConfig();
	}

	private void RestartDevice_Click(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as OnlineConfigViewModel).restartDevice();
	}

	private void ResetVerifyPwd_Click(object sender, RoutedEventArgs e)
	{
		((Window)new VerifyPwdWindow
		{
			toLogin = false
		}).Show();
	}
}
