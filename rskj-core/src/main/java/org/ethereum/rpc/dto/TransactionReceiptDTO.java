/*
 * This file is part of RskJ
 * Copyright (C) 2017 RSK Labs Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.ethereum.rpc.dto;

import co.rsk.core.RskAddress;
import org.ethereum.core.Block;
import org.ethereum.core.TransactionReceipt;
import org.ethereum.db.TransactionInfo;
import org.ethereum.rpc.LogFilterElement;
import org.ethereum.vm.LogInfo;

import static org.ethereum.rpc.TypeConverter.*;


/**
 * Created by Ruben on 5/1/2016.
 */
public class TransactionReceiptDTO {

    public String transactionHash;      // hash of the transaction.
    public String transactionIndex;     // integer of the transactions index position in the block.
    public String blockHash;            // hash of the block where this transaction was in.
    public String blockNumber;          // block number where this transaction was in.
    public String cumulativeGasUsed;    // The total amount of gas used when this transaction was executed in the block.
    public String gasUsed;              //The amount of gas used by this specific transaction alone.
    public String contractAddress;      // The contract address created, if the transaction was a contract creation, otherwise  null .
    public LogFilterElement[] logs;  // Array of log objects, which this transaction generated.
    public String from;
    public String to;
    public String root;
    public String status;


    public  TransactionReceiptDTO(Block block, TransactionInfo txInfo) {

        TransactionReceipt receipt = txInfo.getReceipt();

        status = toQuantityJsonHex(txInfo.getReceipt().getStatus());
        blockHash = toUnformattedJsonHex(txInfo.getBlockHash());
        blockNumber = toQuantityJsonHex(block.getNumber());

        RskAddress contractAddress = receipt.getTransaction().getContractAddress();
        if (contractAddress != null) {
            this.contractAddress = contractAddress.toJsonString();
        }

        cumulativeGasUsed = toQuantityJsonHex(receipt.getCumulativeGas());
        from = receipt.getTransaction().getSender().toJsonString();
        gasUsed = toQuantityJsonHex(receipt.getGasUsed());

        logs = new LogFilterElement[receipt.getLogInfoList().size()];
        for (int i = 0; i < logs.length; i++) {
            LogInfo logInfo = receipt.getLogInfoList().get(i);
            logs[i] = new LogFilterElement(logInfo, block, txInfo.getIndex(),
                    txInfo.getReceipt().getTransaction(), i);
        }

        root = toUnformattedJsonHex(receipt.getPostTxState());
        to = receipt.getTransaction().getReceiveAddress().toJsonString();
        transactionHash = receipt.getTransaction().getHash().toJsonString();
        transactionIndex = toQuantityJsonHex(txInfo.getIndex());
    }
}