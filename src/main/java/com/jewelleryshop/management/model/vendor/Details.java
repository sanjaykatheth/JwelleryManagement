package com.jewelleryshop.management.model.vendor;

import java.net.URL;
import java.util.List;

import com.jewelleryshop.management.enums.StockDetails;
import com.jewelleryshop.management.enums.StockType;

public class Details {

	
	private String profilePhotoUrl;
	private String businessCardPhotoUrl;
	private StockType stockType;
	private String dealPersonName;
	private List<UspArticle> uspArticleList;
	private URL link;
	
}
