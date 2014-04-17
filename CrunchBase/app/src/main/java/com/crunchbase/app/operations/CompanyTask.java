package com.crunchbase.app.operations;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import io.pivotal.arca.service.Task;
import io.pivotal.arca.threading.Identifier;
import com.crunchbase.app.datasets.CompanyTable;
import com.crunchbase.app.models.Company;
import com.crunchbase.app.providers.CrunchBaseContentProvider;
import com.google.gson.Gson;

public class CompanyTask extends Task<String> {

	private final String mId;
	
	public CompanyTask(final String id) {
		mId = id;
	}

	@Override
	public Identifier<?> onCreateIdentifier() {
		return new Identifier<String>(String.format("company::%s", mId));
	}
	
	@Override
	public String onExecuteNetworking(final Context context) throws Exception {
		throw new Exception("Override this method to return a json string for a Company.");
	}

	@Override
	public void onExecuteProcessing(final Context context, final String data) throws Exception {
		final Company item = new Gson().fromJson(data, Company.class);
		final ContentValues values = CompanyTable.getContentValues(item);
		final ContentResolver resolver = context.getContentResolver();
		resolver.insert(CrunchBaseContentProvider.Uris.COMPANIES_URI, values);
	}
}
