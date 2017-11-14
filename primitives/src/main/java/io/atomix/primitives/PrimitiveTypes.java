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

import io.atomix.primitives.map.AsyncAtomicCounterMap;
import io.atomix.primitives.map.AsyncConsistentMap;
import io.atomix.primitives.map.AsyncConsistentTreeMap;
import io.atomix.primitives.map.AtomicCounterMap;
import io.atomix.primitives.map.AtomicCounterMapBuilder;
import io.atomix.primitives.map.ConsistentMap;
import io.atomix.primitives.map.ConsistentMapBuilder;
import io.atomix.primitives.map.ConsistentTreeMap;
import io.atomix.primitives.map.ConsistentTreeMapBuilder;
import io.atomix.primitives.map.impl.DiscreteConsistentMapBuilder;
import io.atomix.primitives.map.impl.PartitionedConsistentMapBuilder;
import io.atomix.primitives.map.impl.RaftConsistentMapService;
import io.atomix.protocols.raft.RaftClient;
import io.atomix.protocols.raft.service.RaftService;

/**
 * Primitive types.
 */
public final class PrimitiveTypes {

  /**
   * Consistent map primitive type.
   */
  public static final PrimitiveType<ConsistentMapBuilder<?, ?>, ConsistentMap<?, ?>, AsyncConsistentMap<?, ?>> CONSISTENT_MAP =
      new RaftPrimitiveType<ConsistentMapBuilder<?, ?>, ConsistentMap<?, ?>, AsyncConsistentMap<?, ?>>() {
        @Override
        public String name() {
          return "map";
        }

        @Override
        public RaftService newRaftService() {
          return new RaftConsistentMapService();
        }

        @Override
        public ConsistentMapBuilder<?, ?> newBuilder(RaftClient client) {
          return new DiscreteConsistentMapBuilder<>(client);
        }

        @Override
        public ConsistentMapBuilder<?, ?> newPartitionedBuilder(RaftClient[] clients) {
          return new PartitionedConsistentMapBuilder<>(clients);
        }
      };

  /**
   * Consistent map primitive type.
   */
  public static final PrimitiveType<ConsistentMapBuilder<?, ?>, ConsistentMap<?, ?>, AsyncConsistentMap<?, ?>> CONSISTENT_MULTIMAP =
      new RaftPrimitiveType<ConsistentMapBuilder<?, ?>, ConsistentMap<?, ?>, AsyncConsistentMap<?, ?>>() {
        @Override
        public String name() {
          return "map";
        }

        @Override
        public RaftService newRaftService() {
          return new RaftConsistentMapService();
        }

        @Override
        public ConsistentMapBuilder<?, ?> newBuilder(RaftClient client) {
          return new DiscreteConsistentMapBuilder<>(client);
        }

        @Override
        public ConsistentMapBuilder<?, ?> newPartitionedBuilder(RaftClient[] clients) {
          return new PartitionedConsistentMapBuilder<>(clients);
        }
      };

  /**
   * Consistent map primitive type.
   */
  public static final PrimitiveType<ConsistentTreeMapBuilder<?, ?>, ConsistentTreeMap<?, ?>, AsyncConsistentTreeMap<?, ?>> CONSISTENT_TREEMAP =
      new RaftPrimitiveType<ConsistentTreeMapBuilder<?, ?>, ConsistentTreeMap<?, ?>, AsyncConsistentTreeMap<?, ?>>() {
        @Override
        public String name() {
          return "map";
        }

        @Override
        public RaftService newRaftService() {
          return new RaftConsistentMapService();
        }

        @Override
        public ConsistentTreeMapBuilder<?, ?> newBuilder(RaftClient client) {
          return new DiscreteConsistentTreeMapBuilder<>(client);
        }

        @Override
        public ConsistentTreeMapBuilder<?, ?> newPartitionedBuilder(RaftClient[] clients) {
          return new PartitionedConsistentTreeMapBuilder<>(clients);
        }
      };

  /**
   * Consistent map primitive type.
   */
  public static final PrimitiveType<AtomicCounterMapBuilder<?>, AtomicCounterMap<?>, AsyncAtomicCounterMap<?>> COUNTER_MAP =
      new RaftPrimitiveType<AtomicCounterMapBuilder<?>, AtomicCounterMap<?>, AsyncAtomicCounterMap<?>>() {
        @Override
        public String name() {
          return "map";
        }

        @Override
        public RaftService newRaftService() {
          return new RaftConsistentMapService();
        }

        @Override
        public AtomicCounterMapBuilder<?> newBuilder(RaftClient client) {
          return new DiscreteAtomicCounterMapBuilder<>(client);
        }

        @Override
        public AtomicCounterMapBuilder<?> newPartitionedBuilder(RaftClient[] clients) {
          return new PartitionedAtomicCounterMapBuilder<>(clients);
        }
      };

  private PrimitiveTypes() {
  }
}
