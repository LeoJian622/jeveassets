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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.nikr.eve.jeveasset.data.api.accounts.OwnerType;
import net.nikr.eve.jeveasset.data.api.my.MyAccountBalance;
import net.nikr.eve.jeveasset.data.api.my.MyAsset;
import net.nikr.eve.jeveasset.data.api.my.MyContract;
import net.nikr.eve.jeveasset.data.api.my.MyContractItem;
import net.nikr.eve.jeveasset.data.api.my.MyExtraction;
import net.nikr.eve.jeveasset.data.api.my.MyIndustryJob;
import net.nikr.eve.jeveasset.data.api.my.MyJournal;
import net.nikr.eve.jeveasset.data.api.my.MyMarketOrder;
import net.nikr.eve.jeveasset.data.api.my.MyMining;
import net.nikr.eve.jeveasset.data.api.my.MyShip;
import net.nikr.eve.jeveasset.data.api.my.MySkill;
import net.nikr.eve.jeveasset.data.api.my.MyTransaction;
import net.nikr.eve.jeveasset.data.api.raw.RawAccountBalance;
import net.nikr.eve.jeveasset.data.api.raw.RawAsset;
import net.nikr.eve.jeveasset.data.api.raw.RawBlueprint;
import net.nikr.eve.jeveasset.data.api.raw.RawContract;
import net.nikr.eve.jeveasset.data.api.raw.RawContractItem;
import net.nikr.eve.jeveasset.data.api.raw.RawExtraction;
import net.nikr.eve.jeveasset.data.api.raw.RawIndustryJob;
import net.nikr.eve.jeveasset.data.api.raw.RawJournal;
import net.nikr.eve.jeveasset.data.api.raw.RawMarketOrder;
import net.nikr.eve.jeveasset.data.api.raw.RawMining;
import net.nikr.eve.jeveasset.data.api.raw.RawPublicMarketOrder;
import net.nikr.eve.jeveasset.data.api.raw.RawSkill;
import net.nikr.eve.jeveasset.data.api.raw.RawTransaction;
import net.nikr.eve.jeveasset.io.shared.DataConverter;
import net.nikr.eve.jeveasset.esi.model.CharacterAssetsResponse;
import net.nikr.eve.jeveasset.esi.model.CharacterBlueprintsResponse;
import net.nikr.eve.jeveasset.esi.model.CharacterContractsItemsResponse;
import net.nikr.eve.jeveasset.esi.model.CharacterContractsResponse;
import net.nikr.eve.jeveasset.esi.model.CharacterIndustryJobsResponse;
import net.nikr.eve.jeveasset.esi.model.CharacterLocationResponse;
import net.nikr.eve.jeveasset.esi.model.CharacterMiningResponse;
import net.nikr.eve.jeveasset.esi.model.CharacterOrdersHistoryResponse;
import net.nikr.eve.jeveasset.esi.model.CharacterOrdersResponse;
import net.nikr.eve.jeveasset.esi.model.CharacterPlanetsResponse;
import net.nikr.eve.jeveasset.esi.model.CharacterShipResponse;
import net.nikr.eve.jeveasset.esi.model.CharacterWalletJournalResponse;
import net.nikr.eve.jeveasset.esi.model.CharacterWalletTransactionsResponse;
import net.nikr.eve.jeveasset.esi.model.CorporationAssetsResponse;
import net.nikr.eve.jeveasset.esi.model.CorporationBlueprintsResponse;
import net.nikr.eve.jeveasset.esi.model.CorporationContractsItemsResponse;
import net.nikr.eve.jeveasset.esi.model.CorporationContractsResponse;
import net.nikr.eve.jeveasset.esi.model.CorporationDivisionsHangar;
import net.nikr.eve.jeveasset.esi.model.CorporationDivisionsWallet;
import net.nikr.eve.jeveasset.esi.model.CorporationIndustryJobsResponse;
import net.nikr.eve.jeveasset.esi.model.CorporationOrdersHistoryResponse;
import net.nikr.eve.jeveasset.esi.model.CorporationOrdersResponse;
import net.nikr.eve.jeveasset.esi.model.CorporationWalletJournalResponse;
import net.nikr.eve.jeveasset.esi.model.CorporationWalletTransactionsResponse;
import net.nikr.eve.jeveasset.esi.model.CorporationWalletsResponse;
import net.nikr.eve.jeveasset.esi.model.MarketOrdersResponse;
import net.nikr.eve.jeveasset.esi.model.PlanetContent;
import net.nikr.eve.jeveasset.esi.model.PlanetPin;
import net.nikr.eve.jeveasset.esi.model.PublicContractsItemsResponse;
import net.nikr.eve.jeveasset.esi.model.Skill;


public class EsiConverter extends DataConverter {

	private EsiConverter() { }

	public static List<MyAccountBalance> toAccountBalance(Double responses, OwnerType owner, Integer accountKey) {
		return convertRawAccountBalance(Collections.singletonList(new RawAccountBalance(responses, accountKey)), owner);
	}

	public static List<MyAccountBalance> toAccountBalanceCorporation(List<CorporationWalletsResponse> responses, OwnerType owner) {
		List<RawAccountBalance> rawAccountBalances = new ArrayList<>();
		for (CorporationWalletsResponse response : responses) {
			rawAccountBalances.add(new RawAccountBalance(response));
		}
		return convertRawAccountBalance(rawAccountBalances, owner);
	}

	public static MyShip toActiveShip(CharacterShipResponse shipType, CharacterLocationResponse shipLocation) {
		return new MyShip(shipType, shipLocation);
	}

	public static List<MyAsset> toAssets(List<CharacterAssetsResponse> responses, OwnerType owner) {
		List<RawAsset> rawAssets = new ArrayList<>();
		for (CharacterAssetsResponse response : responses) {
			rawAssets.add(new RawAsset(response));
		}
		return convertRawAssets(rawAssets, owner);
	}

	public static List<MyAsset> toAssetsCorporation(List<CorporationAssetsResponse> responses, OwnerType owner) {
		List<RawAsset> rawAssets = new ArrayList<>();
		for (CorporationAssetsResponse response : responses) {
			rawAssets.add(new RawAsset(response));
		}
		return convertRawAssets(rawAssets, owner);
	}

	public static MyAsset toAssetsShip(CharacterShipResponse shipType, CharacterLocationResponse shipLocation, OwnerType owner) {
		return toMyAsset(new RawAsset(shipType, shipLocation), owner, new ArrayList<>());
	}

	public static MyAsset toAssetsPlanetaryInteraction(CharacterPlanetsResponse planet, PlanetPin pin, OwnerType owner) {
		MyAsset parent = toMyAsset(new RawAsset(planet, pin), owner, new ArrayList<>());
		List<MyAsset> parents = Collections.singletonList(parent);
		for (PlanetContent content : pin.getContents()) {
			parent.addAsset(toMyAsset(new RawAsset(planet, pin, content), owner, parents));
		}
		return parent;
	}

	public static Map<Long, RawBlueprint> toBlueprints(List<CharacterBlueprintsResponse> responses) {
		Map<Long, RawBlueprint> rawBlueprints = new HashMap<>();
		for (CharacterBlueprintsResponse blueprint : responses) {
			rawBlueprints.put(blueprint.getItemId(), new RawBlueprint(blueprint));
		}
		return rawBlueprints;
	}

	public static Map<Long, RawBlueprint> toBlueprintsCorporation(List<CorporationBlueprintsResponse> responses) {
		Map<Long, RawBlueprint> rawBlueprints = new HashMap<>();
		for (CorporationBlueprintsResponse blueprint : responses) {
			rawBlueprints.put(blueprint.getItemId(), new RawBlueprint(blueprint));
		}
		return rawBlueprints;
	}

	public static List<MyIndustryJob> toIndustryJobs(List<CharacterIndustryJobsResponse> responses, OwnerType owner) {
		List<RawIndustryJob> rawIndustryJobs = new ArrayList<>();
		for (CharacterIndustryJobsResponse response : responses) {
			rawIndustryJobs.add(new RawIndustryJob(response));
		}
		return convertRawIndustryJobs(rawIndustryJobs, owner);
	}

	public static List<MyIndustryJob> toIndustryJobsCorporation(List<CorporationIndustryJobsResponse> responses, OwnerType owner) {
		List<RawIndustryJob> rawIndustryJobs = new ArrayList<>();
		for (CorporationIndustryJobsResponse response : responses) {
			rawIndustryJobs.add(new RawIndustryJob(response));
		}
		return convertRawIndustryJobs(rawIndustryJobs, owner);
	}

	public static Set<MyJournal> toJournals(List<CharacterWalletJournalResponse> responses, OwnerType owner, Integer accountKey, boolean saveHistory) {
		List<RawJournal> rawJournals = new ArrayList<>();
		for (CharacterWalletJournalResponse response : responses) {
			rawJournals.add(new RawJournal(response, accountKey));
		}
		return convertRawJournals(rawJournals, owner, saveHistory);
	}

	public static Set<MyJournal> toJournalsCorporation(List<CorporationWalletJournalResponse> responses, OwnerType owner, Integer accountKey, boolean saveHistory) {
		List<RawJournal> rawJournals = new ArrayList<>();
		for (CorporationWalletJournalResponse response : responses) {
			rawJournals.add(new RawJournal(response, accountKey));
		}
		return convertRawJournals(rawJournals, owner, saveHistory);
	}

	public static Map<MyContract, List<MyContractItem>> toContracts(List<CharacterContractsResponse> responses, OwnerType owner, boolean saveHistory) {
		List<RawContract> rawContracts = new ArrayList<>();
		for (CharacterContractsResponse response : responses) {
			rawContracts.add(new RawContract(response));
		}
		return convertRawContracts(rawContracts, owner, saveHistory);
	}

	public static Map<MyContract, List<MyContractItem>> toContractsCorporation(List<CorporationContractsResponse> responses, OwnerType owner, boolean saveHistory) {
		List<RawContract> rawContracts = new ArrayList<>();
		for (CorporationContractsResponse response : responses) {
			rawContracts.add(new RawContract(response));
		}
		return convertRawContracts(rawContracts, owner, saveHistory);
	}

	public static Map<MyContract, List<MyContractItem>> toContractItems(MyContract contract, List<CharacterContractsItemsResponse> responses, OwnerType owner) {
		List<RawContractItem> rawContractItems = new ArrayList<>();
		for (CharacterContractsItemsResponse response : responses) {
			rawContractItems.add(new RawContractItem(response));
		}
		return convertRawContractItems(contract, rawContractItems, owner);
	}

	public static Map<MyContract, List<MyContractItem>> toContractItemsCorporation(MyContract contract, List<CorporationContractsItemsResponse> responses, OwnerType owner) {
		List<RawContractItem> rawContractItems = new ArrayList<>();
		for (CorporationContractsItemsResponse response : responses) {
			rawContractItems.add(new RawContractItem(response));
		}
		return convertRawContractItems(contract, rawContractItems, owner);
	}

	public static Map<MyContract, List<MyContractItem>> toContractItemsPublic(MyContract contract, List<PublicContractsItemsResponse> responses, OwnerType owner) {
		List<RawContractItem> rawContractItems = new ArrayList<>();
		for (PublicContractsItemsResponse response : responses) {
			rawContractItems.add(new RawContractItem(response));
		}
		return convertRawContractItems(contract, rawContractItems, owner);
	}

	public static Set<MyMarketOrder> toMarketOrders(List<CharacterOrdersResponse> responses, List<CharacterOrdersHistoryResponse> responsesHistory, OwnerType owner, boolean saveHistory) {
		List<RawMarketOrder> rawMarketOrders = new ArrayList<>();
		for (CharacterOrdersResponse response : responses) {
			rawMarketOrders.add(new RawMarketOrder(response));
		}
		for (CharacterOrdersHistoryResponse response : responsesHistory) {
			rawMarketOrders.add(new RawMarketOrder(response));
		}
		return convertRawMarketOrders(rawMarketOrders, owner, saveHistory);
	}

	public static Set<MyMarketOrder> toMarketOrdersCorporation(List<CorporationOrdersResponse> responses, List<CorporationOrdersHistoryResponse> responsesHistory, OwnerType owner, boolean saveHistory) {
		List<RawMarketOrder> rawMarketOrders = new ArrayList<>();
		for (CorporationOrdersResponse response : responses) {
			rawMarketOrders.add(new RawMarketOrder(response));
		}
		for (CorporationOrdersHistoryResponse response : responsesHistory) {
			rawMarketOrders.add(new RawMarketOrder(response));
		}
		return convertRawMarketOrders(rawMarketOrders, owner, saveHistory);
	}

	public static Set<MyTransaction> toTransaction(List<CharacterWalletTransactionsResponse> responses, OwnerType owner, Integer accountKey, boolean saveHistory) {
		List<RawTransaction> rawTransactions = new ArrayList<>();
		for (CharacterWalletTransactionsResponse response : responses) {
			rawTransactions.add(new RawTransaction(response, accountKey));
		}
		return convertRawTransactions(rawTransactions, owner, saveHistory);
	}

	public static Set<MyTransaction> toTransactionCorporation(List<CorporationWalletTransactionsResponse> responses, OwnerType owner, Integer accountKey, boolean saveHistory) {
		List<RawTransaction> rawTransactions = new ArrayList<>();
		for (CorporationWalletTransactionsResponse response : responses) {
			rawTransactions.add(new RawTransaction(response, accountKey));
		}
		return convertRawTransactions(rawTransactions, owner, saveHistory);
	}

	public static Map<Integer, String> toWalletDivisions(List<CorporationDivisionsWallet> divisionsWallets) {
		Map<Integer, String> divisions = new HashMap<>();
		for (CorporationDivisionsWallet response : divisionsWallets) {
			divisions.put(response.getDivision(), response.getName());
		}
		return divisions;
	}

	public static Map<Integer, String> toAssetDivisions(List<CorporationDivisionsHangar> divisionsWallets) {
		Map<Integer, String> divisions = new HashMap<>();
		for (CorporationDivisionsHangar response : divisionsWallets) {
			divisions.put(response.getDivision(), response.getName());
		}
		return divisions;
	}

	public static Map<Integer, Set<RawPublicMarketOrder>> toPublicMarketOrders(List<MarketOrdersResponse> responses) {
		Map<Integer, Set<RawPublicMarketOrder>> marketOrders = new HashMap<>();
		for (MarketOrdersResponse response : responses) {
			RawPublicMarketOrder marketOrder = new RawPublicMarketOrder(response);
			Set<RawPublicMarketOrder> set = marketOrders.get(marketOrder.getTypeID());
			if (set == null) {
				set = new HashSet<>();
				marketOrders.put(marketOrder.getTypeID(), set);
			}
			set.add(marketOrder);
		}
		return marketOrders;
	}

	public static List<MySkill> toSkills(List<Skill> responses, OwnerType owner) {
		List<RawSkill> skills = new ArrayList<>();
		for (Skill response : responses) {
			skills.add(new RawSkill(response));
		}
		return converRawSkills(skills, owner);
	}

	public static List<MyMining> toMining(List<CharacterMiningResponse> responses, OwnerType owner, boolean saveHistory) {
		List<RawMining> mining = new ArrayList<>();
		for (CharacterMiningResponse response : responses) {
			mining.add(new RawMining(response, owner));
		}
		return converRawMining(mining, owner, saveHistory);
	}

	public static List<MyMining> toMining(Map<CorporationMiningObserversResponse, List<CorporationMiningObserverResponse>> responses, OwnerType owner, boolean saveHistory) {
		List<RawMining> mining = new ArrayList<>();
		for (Map.Entry<CorporationMiningObserversResponse, List<CorporationMiningObserverResponse>> response : responses.entrySet()) {
			for (CorporationMiningObserverResponse miningObserver : response.getValue()) {
				mining.add(new RawMining(response.getKey(), miningObserver, owner));
			}
		}
		return converRawMining(mining, owner, saveHistory);
	}

	public static List<MyExtraction> toExtraction(List<CorporationMiningExtractionsResponse> responses, OwnerType owner, boolean saveHistory) {
		List<RawExtraction> extractions = new ArrayList<>();
		for (CorporationMiningExtractionsResponse response : responses) {
			extractions.add(new RawExtraction(response));
		}
		return converRawExtraction(extractions, owner, saveHistory);
	}
}
