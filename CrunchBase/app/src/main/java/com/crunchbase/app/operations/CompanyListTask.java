package com.crunchbase.app.operations;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.crunchbase.app.application.CrunchBaseRequests;
import com.crunchbase.app.datasets.CompanyTable;
import com.crunchbase.app.models.Company;
import com.crunchbase.app.models.SearchResponse;
import com.crunchbase.app.providers.CrunchBaseContentProvider;

import java.util.List;

import io.pivotal.arca.service.Task;
import io.pivotal.arca.threading.Identifier;

public class CompanyListTask extends Task<List<Company>> {

	private final int mPage;

	public CompanyListTask(final int page) {
		mPage = page;
	}

	@Override
	public Identifier<?> onCreateIdentifier() {
		return new Identifier<String>("company_list:" + mPage);
	}
	
	@Override
	public List<Company> onExecuteNetworking(final Context context) throws Exception {
		final SearchResponse response = CrunchBaseRequests.getSearchResults("toronto", mPage);
		final int page = response.getNextPage();
		if (page > 0) {
			addDependency(new CompanyListTask(page));
		}
		return response.getResults();
	}

	@Override
	public void onExecuteProcessing(final Context context, final List<Company> data) throws Exception {
		final ContentValues[] values = CompanyTable.getContentValues(data);
		final ContentResolver resolver = context.getContentResolver();
		resolver.bulkInsert(CrunchBaseContentProvider.Uris.COMPANIES_URI, values);
	}
}
