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
package net.nikr.eve.jeveasset.io.local;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.nikr.eve.jeveasset.gui.tabs.stockpile.Stockpile;
import net.nikr.eve.jeveasset.gui.tabs.stockpile.Stockpile.StockpileFilter;
import net.nikr.eve.jeveasset.gui.tabs.stockpile.Stockpile.StockpileFilter.StockpileContainer;
import net.nikr.eve.jeveasset.gui.tabs.stockpile.Stockpile.StockpileFilter.StockpileFlag;
import net.nikr.eve.jeveasset.gui.tabs.stockpile.Stockpile.StockpileItem;
import net.nikr.eve.jeveasset.io.shared.ApiIdConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.zip.InflaterInputStream;


public class StockpileDataReader extends AbstractBackup {

	private static final Logger LOG = LoggerFactory.getLogger(StockpileDataReader.class);

	public static List<Stockpile> load(String data) {
		StockpileDataReader reader = new StockpileDataReader();
		return reader.read(data);
	}

	private List<Stockpile> read(String data) {
		data = data.trim();
		ByteArrayInputStream inputStream = null;
		try {
			inputStream = new ByteArrayInputStream(Base64.getUrlDecoder().decode(data));
			BufferedReader reader = new BufferedReader(new InputStreamReader(new InflaterInputStream(inputStream)));
			StringBuilder sb = new StringBuilder();
			String str;
			while((str = reader.readLine())!= null) {
				sb.append(str);
				sb.append("\n");
			}
			Gson gson = new GsonBuilder().registerTypeAdapter(Stockpile.class, new StockpileDeserializerGson()).create();
			return gson.fromJson(sb.toString(), new TypeToken<List<Stockpile>>() {}.getType());
		} catch (IllegalArgumentException | JsonSyntaxException | IOException ex) {
			LOG.error(ex.getMessage(), ex);
			return null;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ex) {
					//No problem
				}
			}
		}
	}

		public static class StockpileDeserializerGson implements JsonDeserializer<Stockpile> {

		@Override
		public Stockpile deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			//Stockpile
			JsonObject stockpileObject = json.getAsJsonObject();
			String name = stockpileObject.get("n").getAsString();
			double multiplier = stockpileObject.get("m").getAsDouble();
			boolean contractsMatchAll = false; //NotNull
			if (stockpileObject.has("cma")) {
				contractsMatchAll = stockpileObject.get("cma").getAsBoolean();
			}

			//Filters
			List<StockpileFilter> filters = new ArrayList<>();
			JsonElement filtersElement = stockpileObject.get("sf");
			for (JsonElement filterElement : filtersElement.getAsJsonArray()) {
				JsonObject filterObject = filterElement.getAsJsonObject();
				boolean assets = filterObject.get("a").getAsBoolean();
				boolean boughtContracts = filterObject.get("bbc").getAsBoolean();
				boolean buyingContracts = filterObject.get("bc").getAsBoolean();
				boolean sellingContracts = filterObject.get("sc").getAsBoolean();
				boolean soldContracts = filterObject.get("ssc").getAsBoolean();
				boolean buyOrders = filterObject.get("bo").getAsBoolean();
				boolean sellOrders = filterObject.get("so").getAsBoolean();
				boolean buyTransactions = filterObject.get("bt").getAsBoolean();
				boolean sellTransactions = filterObject.get("st").getAsBoolean();
				boolean exclude = filterObject.get("e").getAsBoolean();
				boolean jobs = filterObject.get("j").getAsBoolean();
				Integer jobsDaysLess = null;
				if (filterObject.has("jdl")) {
					jobsDaysLess = filterObject.get("jdl").getAsInt();
				}
				Integer jobsDaysMore = null;
				if (filterObject.has("jdm")) {
					jobsDaysMore = filterObject.get("jdm").getAsInt();
				}
				Boolean singleton = null;
				if (filterObject.has("s")) {
					singleton = filterObject.get("s").getAsBoolean();
				}
				long locationID = filterObject.get("id").getAsLong();

				//Containers
				List<StockpileContainer> containers = new ArrayList<>();
				JsonElement containersElement = filterObject.get("c");
				for (JsonElement containerElement : containersElement.getAsJsonArray()) {
					JsonObject containerObject = containerElement.getAsJsonObject();
					String container = containerObject.get("cc").getAsString();
					boolean includeSubs = containerObject.get("ic").getAsBoolean();
					containers.add(new StockpileContainer(container, includeSubs));
				}

				//Flags
				List<StockpileFlag> flags = new ArrayList<>();
				JsonElement flagIDsElement = filterObject.get("f");
				for (JsonElement flagElement : flagIDsElement.getAsJsonArray()) {
					if (flagElement.isJsonObject()) {
						JsonObject flagObject = flagElement.getAsJsonObject();
						int flagID = flagObject.get("ff").getAsInt();
						boolean includeSubs = flagObject.get("ic").getAsBoolean();
						flags.add(new StockpileFlag(flagID, includeSubs));
					} else {
						flags.add(new StockpileFlag(flagElement.getAsInt(), true));
					}
				}

				//Owners
				List<Long> ownerIDs = new ArrayList<>();
				JsonElement ownerIDsElement = filterObject.get("o");
				for (JsonElement ownerIdElement : ownerIDsElement.getAsJsonArray()) {
					ownerIDs.add(ownerIdElement.getAsLong());
				}
				filters.add(new StockpileFilter(ApiIdConverter.getLocation(locationID), exclude, flags, containers, ownerIDs, jobsDaysLess, jobsDaysMore, singleton, assets, sellOrders, buyOrders, jobs, buyTransactions, sellTransactions, sellingContracts, soldContracts, buyingContracts, boughtContracts));
			}

			//Create Stockile (then add items)
			Stockpile stockpile = new Stockpile(name, null, filters, multiplier, contractsMatchAll);

			//Items
			JsonElement itemsElement = stockpileObject.get("i");
			for (JsonElement itemElement : itemsElement.getAsJsonArray()) {
				JsonObject itemObject = itemElement.getAsJsonObject();
				int itemTypeID = itemObject.get("i").getAsInt();
				double countMinimum = itemObject.get("m").getAsDouble();
				boolean runs = itemObject.get("r").getAsBoolean();
				JsonElement element = itemObject.get("im");
				boolean ignoreMultiplier = false;
				if (element != null) {
					ignoreMultiplier = element.getAsBoolean();
				}
				stockpile.add(new StockpileItem(stockpile, ApiIdConverter.getItem(Math.abs(itemTypeID)), itemTypeID, countMinimum, runs, ignoreMultiplier));
			}
			return stockpile;
		}
	}
}
