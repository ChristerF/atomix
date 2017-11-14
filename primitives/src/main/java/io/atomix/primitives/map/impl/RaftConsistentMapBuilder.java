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
package io.atomix.primitives.map.impl;

import com.google.common.io.BaseEncoding;
import io.atomix.primitives.DistributedPrimitives;
import io.atomix.primitives.map.AsyncConsistentMap;
import io.atomix.primitives.map.ConsistentMapBuilder;
import io.atomix.protocols.raft.RaftClient;
import io.atomix.protocols.raft.ReadConsistency;
import io.atomix.protocols.raft.proxy.CommunicationStrategy;

import java.time.Duration;

/**
 * Raft consistent map builder.
 */
public abstract class RaftConsistentMapBuilder<K, V> extends ConsistentMapBuilder<K, V> {

  /**
   * Creates a new async consistent map from the given client.
   *
   * @param client the client from which to create the map
   * @return a new async consistent map
   */
  protected AsyncConsistentMap<K, V> newConsistentMap(RaftClient client) {
    RaftConsistentMap rawMap = new RaftConsistentMap(client.newProxyBuilder()
        .withName(name())
        .withServiceType(primitiveType().name())
        .withReadConsistency(ReadConsistency.SEQUENTIAL)
        .withCommunicationStrategy(CommunicationStrategy.ANY)
        .withTimeout(Duration.ofSeconds(30))
        .withMaxRetries(5)
        .build()
        .open()
        .join());

    return DistributedPrimitives.newTranscodingMap(rawMap,
        key -> BaseEncoding.base16().encode(serializer().encode(key)),
        string -> serializer().decode(BaseEncoding.base16().decode(string)),
        value -> value == null ? null : serializer().encode(value),
        bytes -> serializer().decode(bytes));
  }
}
