/*
 * Copyright 2017-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.atomix.transaction;

import io.atomix.impl.CoreTransactionService;
import io.atomix.map.impl.ConsistentTreeMapService;
import io.atomix.primitive.PrimitiveManagementService;
import io.atomix.primitive.PrimitiveType;
import io.atomix.primitive.service.PrimitiveService;
import io.atomix.transaction.impl.DefaultTransactionBuilder;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Transaction primitive type.
 */
public class TransactionType implements PrimitiveType<TransactionBuilder, Transaction> {
  private static final String NAME = "TRANSACTION";
  private static final TransactionType INSTANCE = new TransactionType();

  /**
   * Returns a new consistent tree map type.
   *
   * @return a new consistent tree map type
   */
  public static TransactionType instance() {
    return INSTANCE;
  }

  private TransactionType() {
  }

  @Override
  public String id() {
    return NAME;
  }

  @Override
  public PrimitiveService newService() {
    return new ConsistentTreeMapService();
  }

  @Override
  public TransactionBuilder newPrimitiveBuilder(String name, PrimitiveManagementService managementService) {
    return new DefaultTransactionBuilder(name, managementService, new CoreTransactionService(managementService));
  }

  @Override
  public String toString() {
    return toStringHelper(this)
        .add("id", id())
        .toString();
  }
}
