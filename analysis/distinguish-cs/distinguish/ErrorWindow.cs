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

public class ErrorWindow : Window, IComponentConnector
{
	private string inputText = "";

	private string pwd = "";

	private bool isSetPwd;

	private bool _contentLoaded;

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

	[DebuggerNonUserCode]
	[GeneratedCode("PresentationBuildTasks", "4.0.0.0")]
	public void InitializeComponent()
	{
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Uri uri = new Uri("/distinguish;component/errorwindow.xaml", UriKind.Relative);
			Application.LoadComponent((object)this, uri);
		}
	}

	[DebuggerNonUserCode]
	[GeneratedCode("PresentationBuildTasks", "4.0.0.0")]
	[EditorBrowsable(EditorBrowsableState.Never)]
	void IComponentConnector.Connect(int connectionId, object target)
	{
		//IL_0023: Unknown result type (might be due to invalid IL or missing references)
		//IL_002d: Expected O, but got Unknown
		//IL_002f: Unknown result type (might be due to invalid IL or missing references)
		//IL_003b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0045: Expected O, but got Unknown
		//IL_0047: Unknown result type (might be due to invalid IL or missing references)
		//IL_0053: Unknown result type (might be due to invalid IL or missing references)
		//IL_005d: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
			((UIElement)(ErrorWindow)target).PreviewTextInput += new TextCompositionEventHandler(Window_PreviewTextInput);
			break;
		case 2:
			((UIElement)(TextBox)target).KeyUp += new KeyEventHandler(TextBox_KeyUp);
			break;
		case 3:
			((ButtonBase)(Button)target).Click += new RoutedEventHandler(Button_Click);
			break;
		default:
			_contentLoaded = true;
			break;
		}
	}
}
