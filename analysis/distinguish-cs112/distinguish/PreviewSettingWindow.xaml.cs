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

	internal ComboBox ___CapFpsComboBox;

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

	[DebuggerNonUserCode]
	[GeneratedCode("PresentationBuildTasks", "4.0.0.0")]
	[EditorBrowsable(EditorBrowsableState.Never)]
	void IComponentConnector.Connect(int connectionId, object target)
	{
		//IL_009e: Unknown result type (might be due to invalid IL or missing references)
		//IL_00aa: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b4: Expected Obj, but got Unknown
		//IL_00b6: Unknown result type (might be due to invalid IL or missing references)
		//IL_00c2: Unknown result type (might be due to invalid IL or missing references)
		//IL_00cc: Expected Obj, but got Unknown
		//IL_00ce: Unknown result type (might be due to invalid IL or missing references)
		//IL_00da: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e4: Expected Obj, but got Unknown
		//IL_00e6: Unknown result type (might be due to invalid IL or missing references)
		//IL_00f2: Unknown result type (might be due to invalid IL or missing references)
		//IL_00fc: Expected Obj, but got Unknown
		//IL_00fe: Unknown result type (might be due to invalid IL or missing references)
		//IL_010a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0114: Expected Obj, but got Unknown
		//IL_0116: Unknown result type (might be due to invalid IL or missing references)
		//IL_0122: Unknown result type (might be due to invalid IL or missing references)
		//IL_012c: Expected Obj, but got Unknown
		//IL_012e: Unknown result type (might be due to invalid IL or missing references)
		//IL_013a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0144: Expected Obj, but got Unknown
		//IL_0146: Unknown result type (might be due to invalid IL or missing references)
		//IL_0152: Unknown result type (might be due to invalid IL or missing references)
		//IL_015c: Expected Obj, but got Unknown
		//IL_015e: Unknown result type (might be due to invalid IL or missing references)
		//IL_016a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0174: Expected Obj, but got Unknown
		//IL_0176: Unknown result type (might be due to invalid IL or missing references)
		//IL_018e: Unknown result type (might be due to invalid IL or missing references)
		//IL_019a: Unknown result type (might be due to invalid IL or missing references)
		//IL_01a4: Expected Obj, but got Unknown
		//IL_01a6: Unknown result type (might be due to invalid IL or missing references)
		//IL_01b2: Unknown result type (might be due to invalid IL or missing references)
		//IL_01bc: Expected Obj, but got Unknown
		//IL_01be: Unknown result type (might be due to invalid IL or missing references)
		//IL_01d6: Unknown result type (might be due to invalid IL or missing references)
		//IL_01e2: Unknown result type (might be due to invalid IL or missing references)
		//IL_01ec: Expected Obj, but got Unknown
		//IL_01ee: Unknown result type (might be due to invalid IL or missing references)
		//IL_01fa: Unknown result type (might be due to invalid IL or missing references)
		//IL_0204: Expected Obj, but got Unknown
		//IL_0206: Unknown result type (might be due to invalid IL or missing references)
		//IL_021e: Unknown result type (might be due to invalid IL or missing references)
		//IL_022a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0234: Expected Obj, but got Unknown
		//IL_0236: Unknown result type (might be due to invalid IL or missing references)
		//IL_0242: Unknown result type (might be due to invalid IL or missing references)
		//IL_024c: Expected Obj, but got Unknown
		//IL_024e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0266: Unknown result type (might be due to invalid IL or missing references)
		//IL_0272: Unknown result type (might be due to invalid IL or missing references)
		//IL_027c: Expected Obj, but got Unknown
		//IL_027e: Unknown result type (might be due to invalid IL or missing references)
		//IL_028a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0294: Expected Obj, but got Unknown
		//IL_0296: Unknown result type (might be due to invalid IL or missing references)
		//IL_02ae: Unknown result type (might be due to invalid IL or missing references)
		//IL_02ba: Unknown result type (might be due to invalid IL or missing references)
		//IL_02c4: Expected Obj, but got Unknown
		//IL_02c6: Unknown result type (might be due to invalid IL or missing references)
		//IL_02d2: Unknown result type (might be due to invalid IL or missing references)
		//IL_02dc: Expected Obj, but got Unknown
		//IL_02de: Unknown result type (might be due to invalid IL or missing references)
		//IL_02f6: Unknown result type (might be due to invalid IL or missing references)
		//IL_0302: Unknown result type (might be due to invalid IL or missing references)
		//IL_030c: Expected Obj, but got Unknown
		//IL_030e: Unknown result type (might be due to invalid IL or missing references)
		//IL_031a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0324: Expected Obj, but got Unknown
		//IL_0326: Unknown result type (might be due to invalid IL or missing references)
		//IL_033e: Unknown result type (might be due to invalid IL or missing references)
		//IL_034a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0354: Expected Obj, but got Unknown
		//IL_0356: Unknown result type (might be due to invalid IL or missing references)
		//IL_0362: Unknown result type (might be due to invalid IL or missing references)
		//IL_036c: Expected Obj, but got Unknown
		//IL_036e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0386: Unknown result type (might be due to invalid IL or missing references)
		//IL_0392: Unknown result type (might be due to invalid IL or missing references)
		//IL_039c: Expected Obj, but got Unknown
		//IL_039f: Unknown result type (might be due to invalid IL or missing references)
		//IL_03a9: Expected Obj, but got Unknown
		//IL_03b6: Unknown result type (might be due to invalid IL or missing references)
		//IL_03c0: Expected Obj, but got Unknown
		//IL_03c2: Unknown result type (might be due to invalid IL or missing references)
		//IL_03ce: Unknown result type (might be due to invalid IL or missing references)
		//IL_03d8: Expected Obj, but got Unknown
		//IL_03da: Unknown result type (might be due to invalid IL or missing references)
		//IL_03e6: Unknown result type (might be due to invalid IL or missing references)
		//IL_03f0: Expected Obj, but got Unknown
		//IL_03f2: Unknown result type (might be due to invalid IL or missing references)
		//IL_03fe: Unknown result type (might be due to invalid IL or missing references)
		//IL_0408: Expected Obj, but got Unknown
		switch (connectionId)
		{
		case 1:
			((ButtonBase)(Button)target).Click += YSub;
			break;
		case 2:
			((ButtonBase)(Button)target).Click += YAdd;
			break;
		case 3:
			((ButtonBase)(Button)target).Click += XSub;
			break;
		case 4:
			((ButtonBase)(Button)target).Click += XAdd;
			break;
		case 5:
			((ButtonBase)(Button)target).Click += WidthSub;
			break;
		case 6:
			((ButtonBase)(Button)target).Click += WidthAdd;
			break;
		case 7:
			((ButtonBase)(Button)target).Click += HeightSub;
			break;
		case 8:
			((ButtonBase)(Button)target).Click += HeightAdd;
			break;
		case 9:
			((ButtonBase)(Button)target).Click += ImgRotationSub;
			break;
		case 10:
			((RangeBase)(Slider)target).ValueChanged += Slider_ValueChanged;
			break;
		case 11:
			((ButtonBase)(Button)target).Click += ImgRotationAdd;
			break;
		case 12:
			((ButtonBase)(Button)target).Click += BrightnessSub;
			break;
		case 13:
			((RangeBase)(Slider)target).ValueChanged += Slider_ValueChanged;
			break;
		case 14:
			((ButtonBase)(Button)target).Click += BrightnessAdd;
			break;
		case 15:
			((ButtonBase)(Button)target).Click += CapGainSub;
			break;
		case 16:
			((RangeBase)(Slider)target).ValueChanged += Slider_ValueChanged;
			break;
		case 17:
			((ButtonBase)(Button)target).Click += CapGainAdd;
			break;
		case 18:
			((ButtonBase)(Button)target).Click += CapExposureSub;
			break;
		case 19:
			((RangeBase)(Slider)target).ValueChanged += Slider_ValueChanged;
			break;
		case 20:
			((ButtonBase)(Button)target).Click += CapExposureAdd;
			break;
		case 21:
			((ButtonBase)(Button)target).Click += CapHueSub;
			break;
		case 22:
			((RangeBase)(Slider)target).ValueChanged += Slider_ValueChanged;
			break;
		case 23:
			((ButtonBase)(Button)target).Click += CapHueAdd;
			break;
		case 24:
			((ButtonBase)(Button)target).Click += CapSaturationSub;
			break;
		case 25:
			((RangeBase)(Slider)target).ValueChanged += Slider_ValueChanged;
			break;
		case 26:
			((ButtonBase)(Button)target).Click += CapSaturationAdd;
			break;
		case 27:
			((ButtonBase)(Button)target).Click += CapContrastSub;
			break;
		case 28:
			((RangeBase)(Slider)target).ValueChanged += Slider_ValueChanged;
			break;
		case 29:
			((ButtonBase)(Button)target).Click += CapContrastAdd;
			break;
		case 30:
			((ButtonBase)(Button)target).Click += ImgMinHresholdSub;
			break;
		case 31:
			((RangeBase)(Slider)target).ValueChanged += Slider_ValueChanged;
			break;
		case 32:
			((ButtonBase)(Button)target).Click += ImgMinHresholdAdd;
			break;
		case 33:
			___CapFpsComboBox = (ComboBox)target;
			((Selector)___CapFpsComboBox).SelectionChanged += ComboBox_SelectionChanged;
			break;
		case 34:
			((ButtonBase)(Button)target).Click += MotorSub;
			break;
		case 35:
			((ButtonBase)(Button)target).Click += MotorAdd;
			break;
		case 36:
			((ButtonBase)(Button)target).Click += SaveClick;
			break;
		default:
			_contentLoaded = true;
			break;
		}
	}
}
