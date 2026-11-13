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

public partial class VerifyPwdWindow : Window, IComponentConnector
{
	public bool toLogin = true;


	public VerifyPwdWindow()
	{
		InitializeComponent();
		((DispatcherObject)this).Dispatcher.BeginInvoke((DispatcherPriority)4, (Delegate)(Action)(() =>
		{
			Keyboard.Focus((IInputElement)(object)pwdInput);
		}));
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
}
