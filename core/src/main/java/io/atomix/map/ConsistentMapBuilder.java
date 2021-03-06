/*
 * Copyright 2015-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.atomix.map;

import io.atomix.PrimitiveTypes;
import io.atomix.primitive.Consistency;
import io.atomix.primitive.DistributedPrimitiveBuilder;
import io.atomix.primitive.Persistence;
import io.atomix.primitive.PrimitiveProtocol;
import io.atomix.primitive.Replication;
import io.atomix.protocols.backup.MultiPrimaryProtocol;
import io.atomix.protocols.raft.RaftProtocol;
import io.atomix.protocols.raft.ReadConsistency;
import io.atomix.protocols.raft.proxy.CommunicationStrategy;

import java.time.Duration;

/**
 * Builder for {@link ConsistentMap} instances.
 *
 * @param <K> type for map key
 * @param <V> type for map value
 */
public abstract class ConsistentMapBuilder<K, V>
    extends DistributedPrimitiveBuilder<ConsistentMapBuilder<K, V>, ConsistentMap<K, V>> {

  private boolean nullValues = false;

  public ConsistentMapBuilder(String name) {
    super(PrimitiveTypes.map(), name);
  }

  @Override
  protected Consistency defaultConsistency() {
    return Consistency.SEQUENTIAL;
  }

  @Override
  protected Persistence defaultPersistence() {
    return Persistence.EPHEMERAL;
  }

  @Override
  protected Replication defaultReplication() {
    return Replication.SYNCHRONOUS;
  }

  /**
   * Enables null values in the map.
   *
   * @return this builder
   */
  public ConsistentMapBuilder<K, V> withNullValues() {
    nullValues = true;
    return this;
  }

  /**
   * Returns whether null values are supported by the map.
   *
   * @return {@code true} if null values are supported; {@code false} otherwise
   */
  public boolean nullValues() {
    return nullValues;
  }

  @Override
  public PrimitiveProtocol protocol() {
    PrimitiveProtocol protocol = super.protocol();
    if (protocol != null) {
      return protocol;
    }

    switch (consistency()) {
      case LINEARIZABLE:
        switch (persistence()) {
          case PERSISTENT:
            return newRaftProtocol(Consistency.LINEARIZABLE);
          case EPHEMERAL:
            return newMultiPrimaryProtocol(Consistency.LINEARIZABLE, replication());
        }
      case SEQUENTIAL:
      case EVENTUAL:
        switch (persistence()) {
          case PERSISTENT:
            return newRaftProtocol(Consistency.SEQUENTIAL);
          case EPHEMERAL:
            return newMultiPrimaryProtocol(Consistency.SEQUENTIAL, replication());
          default:
            throw new AssertionError();
        }
      default:
        throw new AssertionError();
    }
  }

  private PrimitiveProtocol newRaftProtocol(Consistency readConsistency) {
    return RaftProtocol.builder()
        .withMinTimeout(Duration.ofSeconds(5))
        .withMaxTimeout(Duration.ofSeconds(30))
        .withReadConsistency(readConsistency == Consistency.LINEARIZABLE
            ? ReadConsistency.LINEARIZABLE_LEASE
            : ReadConsistency.SEQUENTIAL)
        .withCommunicationStrategy(readConsistency == Consistency.SEQUENTIAL
            ? CommunicationStrategy.FOLLOWERS
            : CommunicationStrategy.LEADER)
        .withRecoveryStrategy(recovery())
        .withMaxRetries(maxRetries())
        .withRetryDelay(retryDelay())
        .build();
  }

  private PrimitiveProtocol newMultiPrimaryProtocol(Consistency consistency, Replication replication) {
    return MultiPrimaryProtocol.builder()
        .withConsistency(consistency)
        .withReplication(replication)
        .withRecovery(recovery())
        .withBackups(backups())
        .withMaxRetries(maxRetries())
        .withRetryDelay(retryDelay())
        .build();
  }
}
