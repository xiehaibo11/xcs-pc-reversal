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

public class PreviewWindow : Window, IComponentConnector
{
	private PreviewSettingViewModel previewSettingViewModel;

	private string ipAddr = string.Empty;

	private bool _contentLoaded;

	public PreviewWindow(string ipAddr, PreviewSettingViewModel preview)
	{
		this.ipAddr = ipAddr;
		previewSettingViewModel = preview;
		InitializeComponent();
	}

	private void Button_Click(object sender, RoutedEventArgs e)
	{
		sendMsg(1);
	}

	private void Button_Click_1(object sender, RoutedEventArgs e)
	{
		sendMsg(2);
	}

	private void Button_Click_2(object sender, RoutedEventArgs e)
	{
		sendMsg(3);
	}

	private void Button_Click_3(object sender, RoutedEventArgs e)
	{
		sendMsg(4);
	}

	private void sendMsg(int dir)
	{
		//IL_0068: Unknown result type (might be due to invalid IL or missing references)
		foreach (Device device in Global.devices)
		{
			if (device.Direction == dir)
			{
				previewSettingViewModel.getServerData(device);
				((Window)new PreviewSettingWindow(previewSettingViewModel)).ShowDialog();
				previewSettingViewModel.endPreview();
				return;
			}
		}
		MessageBox.Show("预览错误");
	}

	[DebuggerNonUserCode]
	[GeneratedCode("PresentationBuildTasks", "4.0.0.0")]
	public void InitializeComponent()
	{
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Uri uri = new Uri("/distinguish;component/previewwindow.xaml", UriKind.Relative);
			Application.LoadComponent((object)this, uri);
		}
	}

	[DebuggerNonUserCode]
	[GeneratedCode("PresentationBuildTasks", "4.0.0.0")]
	[EditorBrowsable(EditorBrowsableState.Never)]
	void IComponentConnector.Connect(int connectionId, object target)
	{
		//IL_001b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0027: Unknown result type (might be due to invalid IL or missing references)
		//IL_0031: Expected O, but got Unknown
		//IL_0033: Unknown result type (might be due to invalid IL or missing references)
		//IL_003f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0049: Expected O, but got Unknown
		//IL_004b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0057: Unknown result type (might be due to invalid IL or missing references)
		//IL_0061: Expected O, but got Unknown
		//IL_0063: Unknown result type (might be due to invalid IL or missing references)
		//IL_006f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0079: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
			((ButtonBase)(Button)target).Click += new RoutedEventHandler(Button_Click);
			break;
		case 2:
			((ButtonBase)(Button)target).Click += new RoutedEventHandler(Button_Click_1);
			break;
		case 3:
			((ButtonBase)(Button)target).Click += new RoutedEventHandler(Button_Click_2);
			break;
		case 4:
			((ButtonBase)(Button)target).Click += new RoutedEventHandler(Button_Click_3);
			break;
		default:
			_contentLoaded = true;
			break;
		}
	}
}
