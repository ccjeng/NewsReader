package com.oddsoft.news;

import java.io.IOException;

public interface INewsBody {	
	public String loadHtml(String link) throws IOException;
	}
