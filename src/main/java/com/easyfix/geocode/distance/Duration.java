package com.easyfix.geocode.distance;

public class Duration {
	private String text;

    private String value;

    public String getText ()
    {
        return text;
    }

    public void setText (String text)
    {
        this.text = text;
    }

    public String getValue ()
    {
        return value;
    }

    public void setValue (String value)
    {
        this.value = value;
    }

	@Override
	public String toString() {
		return " [duration_text=" + text + ",duration_value =" + value
				 + "]";
	}
    
}