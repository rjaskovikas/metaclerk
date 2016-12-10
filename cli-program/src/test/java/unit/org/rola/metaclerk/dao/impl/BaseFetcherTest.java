package org.rola.metaclerk.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.rola.metaclerk.dao.api.JdbcConnector;


public class BaseFetcherTest extends BaseFetcher {

	@Test
	public void checkSetter() {
		JdbcConnector cMock = mock(JdbcConnector.class);
		setConnector(cMock);
		assertEquals(cMock, dbc);
	}

}
