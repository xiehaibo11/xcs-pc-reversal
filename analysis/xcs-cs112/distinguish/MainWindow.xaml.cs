using System.CodeDom.Compiler;
using System.ComponentModel;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Markup;
using distinguish.Common;
using distinguish.ViewModel;

namespace distinguish;

public partial class MainWindow : Window, IComponentConnector
{
	internal TextBox ipAddrInput;

	internal TextBox userInput;

	internal TextBox pwdInput;

	internal Button loginBut;

	internal Button resetBut;

	public MainWindow()
	{
		Global.RemoveCacheImg();
		if (!Global.verify)
		{
			((Window)new ErrorWindow()).Show();
			((Window)this).Close();
		}
		InitializeComponent();
		((FrameworkElement)this).DataContext = new LoginViewModel();
	}

	private void CheckBox_Checked(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as LoginViewModel).SavePwdCheckBox = true;
	}

	private void CheckBox_Unchecked(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as LoginViewModel).SavePwdCheckBox = false;
	}

	private void Button_Click(object sender, RoutedEventArgs e)
	{
		Application.Current.Shutdown();
	}
}
