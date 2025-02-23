/*
 * Copyright 2009-2023 Contributors (see credits.txt)
 *
 * This file is part of jEveAssets.
 *
 * jEveAssets is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * jEveAssets is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jEveAssets; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */
package net.nikr.eve.jeveasset.io.esi;

import java.awt.Window;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Base64;
import java.util.Set;
import java.util.UUID;
import net.nikr.eve.jeveasset.data.api.accounts.EsiOwner;
import net.nikr.eve.jeveasset.io.shared.DesktopUtil;
import net.troja.eve.esi.auth.OAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EsiAuth {

	private static final Logger LOG = LoggerFactory.getLogger(EsiAuth.class);

	private OAuth oAuth;
	private final MicroServe microServe;
	private EsiCallbackURL callbackURL;

	public EsiAuth() {
		microServe = new MicroServe();
		microServe.startServer();
	}

	public void cancelImport() {
		microServe.stopListening();
	}

	public boolean isServerStarted() {
		return microServe.isServerStarted();
	}

	public boolean openWebpage(EsiCallbackURL callbackURL, Set<String> scopes, Window window) {
		try {
			if (callbackURL == EsiCallbackURL.LOCALHOST) {
				microServe.startListening();
			}
			oAuth = new OAuth(); //We always need a new oAuth to start a new flow
			this.callbackURL = callbackURL;
			oAuth.setClientId(callbackURL.getA());
			String state = UUID.randomUUID().toString();
			String authorizationUri = oAuth.getAuthorizationUri(callbackURL.getUrl(), scopes, state);
			DesktopUtil.browse("https://login.evepc.163.com/account/logoff", window);
			Thread.sleep(1000);
			return DesktopUtil.browse(authorizationUri, window);
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
			return false;
		}
	}

	public boolean finishFlow(EsiOwner esiOwner, String authCode) {
		if (oAuth == null) {
			return false;
		}
		String code;
		if (callbackURL == EsiCallbackURL.LOCALHOST) {
			code = microServe.getAuthCode();
			if (code == null) {
				return false;
			}
		} else {
			//				code = new String(Base64.getUrlDecoder().decode(authCode), "UTF-8");
			URL url;
			try {
				url = new URL(authCode);
				String query = url.getQuery();
				String[] split = query.split("&");
				authCode = split[0].replace("code=", "");
			} catch (NullPointerException | MalformedURLException ex) {
				LOG.error(ex.getMessage(), ex);
				return false;
			}

			code = authCode;
		}
		try {
			oAuth.finishFlow(code, "jeveassets");
			esiOwner.setAuth(callbackURL, oAuth.getRefreshToken(), oAuth.getAccessToken());
			return true;
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
			return false;
		}
	}
}
