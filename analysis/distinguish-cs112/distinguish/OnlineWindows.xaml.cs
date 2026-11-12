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

	[DebuggerNonUserCode]
	[GeneratedCode("PresentationBuildTasks", "4.0.0.0")]
	[EditorBrowsable(EditorBrowsableState.Never)]
	void IComponentConnector.Connect(int connectionId, object target)
	{
		//IL_001f: Unknown result type (might be due to invalid IL or missing references)
		//IL_002b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0035: Expected Obj, but got Unknown
		//IL_0037: Unknown result type (might be due to invalid IL or missing references)
		//IL_0043: Unknown result type (might be due to invalid IL or missing references)
		//IL_004d: Expected Obj, but got Unknown
		//IL_004f: Unknown result type (might be due to invalid IL or missing references)
		//IL_005b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0065: Expected Obj, but got Unknown
		//IL_0067: Unknown result type (might be due to invalid IL or missing references)
		//IL_0073: Unknown result type (might be due to invalid IL or missing references)
		//IL_007d: Expected Obj, but got Unknown
		//IL_007f: Unknown result type (might be due to invalid IL or missing references)
		//IL_008b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0095: Expected Obj, but got Unknown
		switch (connectionId)
		{
		case 1:
			((ButtonBase)(Button)target).Click += SaveConfigNumber_Click;
			break;
		case 2:
			((ButtonBase)(Button)target).Click += SaveDataConfig_Click;
			break;
		case 3:
			((ButtonBase)(Button)target).Click += SaveWifi_Click;
			break;
		case 4:
			((ButtonBase)(Button)target).Click += RestartDevice_Click;
			break;
		case 5:
			((ButtonBase)(Button)target).Click += ResetVerifyPwd_Click;
			break;
		default:
			_contentLoaded = true;
			break;
		}
	}
}
