using System;
using System.CodeDom.Compiler;
using System.ComponentModel;
using System.Diagnostics;
using System.Media;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Input;
using System.Windows.Markup;
using distinguish.Common;

namespace distinguish;

public partial class ErrorWindow : Window, IComponentConnector
{
	private string inputText = "";

	private string pwd = "";

	private bool isSetPwd;

	public ErrorWindow()
	{
		SystemSounds.Beep.Play();
		InitializeComponent();
		if (AppConfig.getConfig(Constant.INI_VERIFY_ROOT, Constant.INI_VERIFY_PWD) == "")
		{
			pwd = Global.verifyDefaultPwd;
			return;
		}
		pwd = AppConfig.getConfig(Constant.INI_VERIFY_ROOT, Constant.INI_VERIFY_PWD);
		isSetPwd = true;
	}

	private void Button_Click(object sender, RoutedEventArgs e)
	{
		Application.Current.Shutdown();
	}

	private void TextBox_KeyUp(object sender, KeyEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		Console.WriteLine(e.Key);
	}

	private void Window_PreviewTextInput(object sender, TextCompositionEventArgs e)
	{
		inputText += e.Text;
		if (Global.GetMd5(inputText) == pwd)
		{
			Global.verify = true;
			if (isSetPwd)
			{
				((Window)new MainWindow()).Show();
			}
			else
			{
				((Window)new VerifyPwdWindow()).Show();
			}
			((Window)this).Close();
		}
	}
}
