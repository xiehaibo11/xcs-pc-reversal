using System;
using System.CodeDom.Compiler;
using System.ComponentModel;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Input;
using System.Windows.Markup;
using System.Windows.Threading;
using distinguish.Common;

namespace distinguish;

public class VerifyPwdWindow : Window, IComponentConnector
{
	public bool toLogin = true;

	internal TextBox pwdInput;

	private bool _contentLoaded;

	public VerifyPwdWindow()
	{
		InitializeComponent();
		((DispatcherObject)this).Dispatcher.BeginInvoke((DispatcherPriority)4, (Delegate)(Action)delegate
		{
			Keyboard.Focus((IInputElement)(object)pwdInput);
		});
	}

	private void Button_Click(object sender, RoutedEventArgs e)
	{
		_save();
	}

	private void pwdInput_KeyUp(object sender, KeyEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Invalid comparison between Unknown and I4
		if ((int)e.Key == 6)
		{
			_save();
		}
	}

	private void _save()
	{
		//IL_001c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0052: Unknown result type (might be due to invalid IL or missing references)
		if (pwdInput.Text == "")
		{
			MessageBox.Show("请输入专属密钥");
			((UIElement)pwdInput).Focus();
			return;
		}
		string md = Global.GetMd5(pwdInput.Text);
		if (md == Global.verifyDefaultPwd)
		{
			MessageBox.Show("专属密钥不能与初始化密钥一致！");
			return;
		}
		AppConfig.setConfig(Constant.INI_VERIFY_ROOT, Constant.INI_VERIFY_PWD, md);
		if (toLogin)
		{
			((Window)new MainWindow()).Show();
		}
		((Window)this).Close();
	}

	[DebuggerNonUserCode]
	[GeneratedCode("PresentationBuildTasks", "4.0.0.0")]
	public void InitializeComponent()
	{
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Uri uri = new Uri("/xcs;component/verifypwdwindow.xaml", UriKind.Relative);
			Application.LoadComponent((object)this, uri);
		}
	}

	[DebuggerNonUserCode]
	[GeneratedCode("PresentationBuildTasks", "4.0.0.0")]
	[EditorBrowsable(EditorBrowsableState.Never)]
	void IComponentConnector.Connect(int connectionId, object target)
	{
		//IL_000c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0016: Expected O, but got Unknown
		//IL_0023: Unknown result type (might be due to invalid IL or missing references)
		//IL_002d: Expected O, but got Unknown
		//IL_002f: Unknown result type (might be due to invalid IL or missing references)
		//IL_003b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0045: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
			pwdInput = (TextBox)target;
			((UIElement)pwdInput).KeyUp += new KeyEventHandler(pwdInput_KeyUp);
			break;
		case 2:
			((ButtonBase)(Button)target).Click += new RoutedEventHandler(Button_Click);
			break;
		default:
			_contentLoaded = true;
			break;
		}
	}
}
