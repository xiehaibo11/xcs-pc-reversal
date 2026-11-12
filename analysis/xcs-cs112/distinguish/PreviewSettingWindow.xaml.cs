using System.CodeDom.Compiler;
using System.ComponentModel;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Markup;
using distinguish.ViewModel;

namespace distinguish;

public partial class PreviewSettingWindow : Window, IComponentConnector
{
	private PreviewSettingViewModel previewSettingViewModel;


	public PreviewSettingWindow(PreviewSettingViewModel previewSettingViewModel)
	{
		previewSettingViewModel.DataStatus = (Visibility)0;
		this.previewSettingViewModel = previewSettingViewModel;
		((FrameworkElement)this).DataContext = previewSettingViewModel;
		InitializeComponent();
		switch (previewSettingViewModel.CapFps)
		{
		case 60:
			((Selector)___CapFpsComboBox).SelectedIndex = 0;
			break;
		case 70:
			((Selector)___CapFpsComboBox).SelectedIndex = 1;
			break;
		case 80:
			((Selector)___CapFpsComboBox).SelectedIndex = 2;
			break;
		case 90:
			((Selector)___CapFpsComboBox).SelectedIndex = 3;
			break;
		case 180:
			((Selector)___CapFpsComboBox).SelectedIndex = 4;
			break;
		default:
			((Selector)___CapFpsComboBox).SelectedIndex = 0;
			break;
		}
	}

	private void BrightnessAdd(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as PreviewSettingViewModel).CapBrightness++;
		onSaveHttp();
	}

	private void BrightnessSub(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as PreviewSettingViewModel).CapBrightness--;
		onSaveHttp();
	}

	private void ImgMinHresholdAdd(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as PreviewSettingViewModel).ImgMinHreshold++;
		onSaveHttp();
	}

	private void ImgMinHresholdSub(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as PreviewSettingViewModel).ImgMinHreshold--;
		onSaveHttp();
	}

	private void CapExposureAdd(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as PreviewSettingViewModel).CapExposure++;
		onSaveHttp();
	}

	private void CapExposureSub(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as PreviewSettingViewModel).CapExposure--;
		onSaveHttp();
	}

	private void CapHueAdd(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as PreviewSettingViewModel).CapHue++;
		onSaveHttp();
	}

	private void CapHueSub(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as PreviewSettingViewModel).CapHue--;
		onSaveHttp();
	}

	private void CapSaturationAdd(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as PreviewSettingViewModel).CapSaturation++;
		onSaveHttp();
	}

	private void CapSaturationSub(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as PreviewSettingViewModel).CapSaturation--;
		onSaveHttp();
	}

	private void CapContrastAdd(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as PreviewSettingViewModel).CapContrast++;
		onSaveHttp();
	}

	private void CapContrastSub(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as PreviewSettingViewModel).CapContrast--;
		onSaveHttp();
	}

	private void CapGainAdd(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as PreviewSettingViewModel).CapGain++;
		onSaveHttp();
	}

	private void CapGainSub(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as PreviewSettingViewModel).CapGain--;
		onSaveHttp();
	}

	private void ImgRotationAdd(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as PreviewSettingViewModel).ImgRotation++;
		onSaveHttp();
	}

	private void ImgRotationSub(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as PreviewSettingViewModel).ImgRotation--;
		onSaveHttp();
	}

	private void HeightAdd(object sender, RoutedEventArgs e)
	{
		PreviewSettingViewModel previewSettingViewModel = ((FrameworkElement)this).DataContext as PreviewSettingViewModel;
		if (previewSettingViewModel.Height >= 0 && previewSettingViewModel.Height <= 50)
		{
			previewSettingViewModel.Height++;
		}
	}

	private void HeightSub(object sender, RoutedEventArgs e)
	{
		PreviewSettingViewModel previewSettingViewModel = ((FrameworkElement)this).DataContext as PreviewSettingViewModel;
		if (previewSettingViewModel.Height >= 0 && previewSettingViewModel.Height <= 50)
		{
			previewSettingViewModel.Height--;
		}
	}

	private void MotorAdd(object sender, RoutedEventArgs e)
	{
		PreviewSettingViewModel previewSettingViewModel = ((FrameworkElement)this).DataContext as PreviewSettingViewModel;
		if (previewSettingViewModel.Motor >= 0 && previewSettingViewModel.Motor <= 4900)
		{
			previewSettingViewModel.Motor += 100;
		}
	}

	private void MotorSub(object sender, RoutedEventArgs e)
	{
		PreviewSettingViewModel previewSettingViewModel = ((FrameworkElement)this).DataContext as PreviewSettingViewModel;
		if (previewSettingViewModel.Motor >= 0 && previewSettingViewModel.Motor <= 4900)
		{
			previewSettingViewModel.Motor -= 100;
		}
	}

	private void WidthAdd(object sender, RoutedEventArgs e)
	{
		PreviewSettingViewModel previewSettingViewModel = ((FrameworkElement)this).DataContext as PreviewSettingViewModel;
		if (previewSettingViewModel.Width >= 0 && previewSettingViewModel.Width <= 300)
		{
			previewSettingViewModel.Width++;
		}
	}

	private void WidthSub(object sender, RoutedEventArgs e)
	{
		PreviewSettingViewModel previewSettingViewModel = ((FrameworkElement)this).DataContext as PreviewSettingViewModel;
		if (previewSettingViewModel.Width >= 0 && previewSettingViewModel.Width <= 300)
		{
			previewSettingViewModel.Width--;
		}
	}

	private void XAdd(object sender, RoutedEventArgs e)
	{
		PreviewSettingViewModel previewSettingViewModel = ((FrameworkElement)this).DataContext as PreviewSettingViewModel;
		if (previewSettingViewModel.X >= 0 && previewSettingViewModel.X <= 320)
		{
			previewSettingViewModel.X++;
		}
	}

	private void XSub(object sender, RoutedEventArgs e)
	{
		PreviewSettingViewModel previewSettingViewModel = ((FrameworkElement)this).DataContext as PreviewSettingViewModel;
		if (previewSettingViewModel.X >= 0 && previewSettingViewModel.X <= 320)
		{
			previewSettingViewModel.X--;
		}
	}

	private void YAdd(object sender, RoutedEventArgs e)
	{
		PreviewSettingViewModel previewSettingViewModel = ((FrameworkElement)this).DataContext as PreviewSettingViewModel;
		if (previewSettingViewModel.Y >= 0 && previewSettingViewModel.Y <= 320)
		{
			previewSettingViewModel.Y++;
		}
	}

	private void YSub(object sender, RoutedEventArgs e)
	{
		PreviewSettingViewModel previewSettingViewModel = ((FrameworkElement)this).DataContext as PreviewSettingViewModel;
		if (previewSettingViewModel.Y >= 0 && previewSettingViewModel.Y <= 320)
		{
			previewSettingViewModel.Y--;
		}
	}

	private void SaveClick(object sender, RoutedEventArgs e)
	{
		PreviewSettingViewModel previewSettingViewModel = ((FrameworkElement)this).DataContext as PreviewSettingViewModel;
		if (previewSettingViewModel.saveParams(alert: true))
		{
			previewSettingViewModel.DataStatus = (Visibility)0;
			((Window)this).Close();
		}
	}

	private void onSaveHttp()
	{
	}

	private void Slider_ValueChanged(object sender, RoutedPropertyChangedEventArgs<double> e)
	{
	}

	private void ComboBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
	{
		previewSettingViewModel = ((FrameworkElement)this).DataContext as PreviewSettingViewModel;
		switch (((Selector)((sender is ComboBox) ? sender : null)).SelectedIndex)
		{
		case 0:
			previewSettingViewModel.CapFps = 60;
			break;
		case 1:
			previewSettingViewModel.CapFps = 70;
			break;
		case 2:
			previewSettingViewModel.CapFps = 80;
			break;
		case 3:
			previewSettingViewModel.CapFps = 90;
			break;
		case 4:
			previewSettingViewModel.CapFps = 180;
			break;
		default:
			previewSettingViewModel.CapFps = 60;
			break;
		}
	}
}
