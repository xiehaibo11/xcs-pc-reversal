using System;
using System.Globalization;
using System.Windows.Data;

namespace distinguish;

public class RadioToBoolConverter : IValueConverter
{
	public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
	{
		return value != null && (value.ToString()?.Equals(parameter) ?? false);
	}

	public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
	{
		if (value == null || !value.Equals(true))
		{
			return Binding.DoNothing;
		}
		return parameter;
	}
}
