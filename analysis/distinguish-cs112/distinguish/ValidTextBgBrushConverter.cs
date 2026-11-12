using System;
using System.Globalization;
using System.Windows.Data;
using System.Windows.Media;

namespace distinguish;

internal class ValidTextBgBrushConverter : IValueConverter
{
	public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
	{
		if (value is int num && num != 0 && num < 4)
		{
			return Brushes.Brown;
		}
		if (value is int num2 && num2 != 0 && num2 > 4)
		{
			return Brushes.DarkOrange;
		}
		if (value is int num3 && num3 == 4)
		{
			return Brushes.LightSlateGray;
		}
		return Brushes.DimGray;
	}

	public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
	{
		throw new NotImplementedException();
	}
}
