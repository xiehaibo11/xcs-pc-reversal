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

public partial class PreviewWindow : Window, IComponentConnector
{
	private PreviewSettingViewModel previewSettingViewModel;

	private string ipAddr = string.Empty;

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
}
