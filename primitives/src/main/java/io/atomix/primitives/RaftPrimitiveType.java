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
package io.atomix.primitives;

import io.atomix.protocols.raft.RaftClient;
import io.atomix.protocols.raft.service.RaftService;

/**
 * Raft primitive type.
 */
public interface RaftPrimitiveType<B extends DistributedPrimitiveBuilder<B, S, A>, S extends SyncPrimitive, A extends AsyncPrimitive> extends PrimitiveType<B, S, A> {

  /**
   * Returns a new Raft service.
   *
   * @return a new Raft service
   */
  RaftService newRaftService();

  /**
   * Creates a new primitive builder.
   *
   * @param client the Raft client
   * @return the primitive builder
   */
  B newBuilder(RaftClient client);

  /**
   * Creates a new primitive builder.
   *
   * @param clients a list of partition clients
   * @return the primitive builder
   */
  B newPartitionedBuilder(RaftClient[] clients);

}
