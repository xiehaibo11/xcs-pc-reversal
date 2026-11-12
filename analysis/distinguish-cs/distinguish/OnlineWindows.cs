using System;
using System.CodeDom.Compiler;
using System.ComponentModel;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Markup;
using distinguish.ViewModel;

namespace distinguish;

public class OnlineWindows : Window, IComponentConnector
{
	private bool _contentLoaded;

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

	[DebuggerNonUserCode]
	[GeneratedCode("PresentationBuildTasks", "4.0.0.0")]
	public void InitializeComponent()
	{
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Uri uri = new Uri("/distinguish;component/onlinewindows.xaml", UriKind.Relative);
			Application.LoadComponent((object)this, uri);
		}
	}

	[DebuggerNonUserCode]
	[GeneratedCode("PresentationBuildTasks", "4.0.0.0")]
	[EditorBrowsable(EditorBrowsableState.Never)]
	void IComponentConnector.Connect(int connectionId, object target)
	{
		//IL_001f: Unknown result type (might be due to invalid IL or missing references)
		//IL_002b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0035: Expected O, but got Unknown
		//IL_0037: Unknown result type (might be due to invalid IL or missing references)
		//IL_0043: Unknown result type (might be due to invalid IL or missing references)
		//IL_004d: Expected O, but got Unknown
		//IL_004f: Unknown result type (might be due to invalid IL or missing references)
		//IL_005b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0065: Expected O, but got Unknown
		//IL_0067: Unknown result type (might be due to invalid IL or missing references)
		//IL_0073: Unknown result type (might be due to invalid IL or missing references)
		//IL_007d: Expected O, but got Unknown
		//IL_007f: Unknown result type (might be due to invalid IL or missing references)
		//IL_008b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0095: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
			((ButtonBase)(Button)target).Click += new RoutedEventHandler(SaveConfigNumber_Click);
			break;
		case 2:
			((ButtonBase)(Button)target).Click += new RoutedEventHandler(SaveDataConfig_Click);
			break;
		case 3:
			((ButtonBase)(Button)target).Click += new RoutedEventHandler(SaveWifi_Click);
			break;
		case 4:
			((ButtonBase)(Button)target).Click += new RoutedEventHandler(RestartDevice_Click);
			break;
		case 5:
			((ButtonBase)(Button)target).Click += new RoutedEventHandler(ResetVerifyPwd_Click);
			break;
		default:
			_contentLoaded = true;
			break;
		}
	}
}
