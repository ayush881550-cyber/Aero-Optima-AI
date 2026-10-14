# AeroOptima AI ✈️
### Intelligent Air Operations & Resource Optimisation Simulator
**Smart India Hackathon (SIH) — Problem Statement SIH26250**  
*Air Power: Dynamic Air Operations & Resource Optimisation*

---

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84.svg?style=flat&logo=android)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20M3-4285F4.svg?style=flat&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Optimization](https://img.shields.io/badge/Solver-Mathematical%20MIP%20%2B%20Heuristic-00E5FF.svg?style=flat)]()
[![Tests](https://img.shields.io/badge/Tests-Robolectric%20Passing-success.svg?style=flat)]()
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

> **IMPORTANT DISCLAIMER**: This application is strictly an educational operations research simulator developed for Smart India Hackathon (SIH26250). It uses **100% synthetic/demo data** and does **not** connect to real-world military command systems or provide real-world tactical/combat recommendations.

---

## 📌 Executive Summary

**AeroOptima AI** is an advanced operations research and resource allocation platform built to solve the complex mathematical challenge of assigning constrained, heterogeneous air assets to mission tasks under dynamic operational uncertainty.

In realistic scenarios, manual or uncoordinated First-Come-First-Served (FIFO) planning leads to:
- Severe **priority inversions** (routine tasks blocking critical missions).
- Excessive **turnaround downtime** and fragmented fleet utilization.
- Cascade delays when unexpected disruptions occur (e.g., sudden asset groundings or emergency scrambles).

AeroOptima AI solves this using a **transparent mathematical optimization engine** combined with an aerospace command dashboard, interactive Gantt timeline, Explainable AI (XAI) decision rationales, and live disruption injection.

---

## 🌟 Core Features

### 1. Interactive Operations Command Dashboard
- **Real-Time KPIs**: Simulated mission completion rate, fleet utilization percentage, total delays avoided, and mathematical objective score ($Z$).
- **Before vs. After Optimization**: Instant delta analysis comparing naive baseline scheduling against the AeroOptima solver.
- **Fleet Load Chart**: Canvas-rendered dual-track progress bars visualizing active sortie hours, turnaround maintenance, and idle waste.
- **Automated Bottleneck Detection**: Real-time alerts for over-capacity time windows, grounded assets, and tight deadline envelopes with concrete mitigation recommendations.

### 2. Operations Timeline & Interactive Gantt Chart
- **Synchronized Time Axis**: Real-time horizontal timeline spanning $T+0$ to $T+360$ minutes.
- **Asset Swimlanes**: Dedicated lanes per resource showing scheduled task blocks color-coded by priority level:
  - 🔴 **Critical** (Weight: 10.0x)
  - 🟠 **High** (Weight: 5.0x)
  - 🔵 **Medium** (Weight: 2.5x)
  - 🔷 **Low** (Weight: 1.0x)
- **Turnaround Cooldown Visualizer**: Explicitly renders required post-mission maintenance, inspection, and refueling buffers (20m–45m) to ensure flight readiness.
- **Interactive Inspector**: Tap any flight block to inspect mission parameters, start/end windows, delays, and mathematical solver rationales.

### 3. Transparent AI & Mathematical Optimization Engine
- **No Black-Box or Fake ML**: Clear distinction between mathematical optimization and machine learning.
- **Multi-Objective Mixed-Integer Constraint Formulation**:
  $$\min Z = \sum_{i} \left( w_i \cdot \text{Delay}_i + P_{\text{unassigned}} \cdot u_i \right) + \lambda \sum_{j} \text{IdleFragmentation}_j - \mu \sum_i (w_i \cdot s_i)$$
- **Enforced Hard Constraints**:
  1. *Role Compatibility*: $x_{ijt} = 1 \implies \text{Role}(j) = \text{RequiredRole}(i)$
  2. *Turnaround Buffer*: $t_{\text{start}}(b) \ge t_{\text{end}}(a) + \text{Turnaround}(j)$
  3. *Temporal Feasibility*: $t_{\text{start}} \ge \text{EarliestStart}$ and $t_{\text{end}} \le \text{Deadline}$
  4. *Capacity Limits*: Active concurrent sorties $\le$ Total asset capacity.
- **Explainable AI (XAI) Rationale Engine**: Every assignment includes an explicit decision log explaining why asset $j$ was selected, which alternatives were evaluated and rejected, and which constraints were satisfied.

### 4. Dynamic Scenario Lab & Disruption Simulator
- **One-Click Presets**:
  - `SIH-26250 Demo Scenario`: Balanced multi-role fleet (7 missions, 4 assets).
  - `High-Stress Surge Crisis`: Over-capacity demand testing bottleneck preservation for Critical sorties.
  - `Fleet Disruption & Grounding`: Primary UAV wing grounded for emergency maintenance.
- **Real-Time Disruption Triggers**:
  - **Scramble Emergency QRA Alert**: Injects high-priority scramble missions to demonstrate dynamic real-time rescheduling.
  - **Ground / Restore Fleet Units**: One-tap toggling of resource availability to test resilience.
- **Scenario Customizer**: Add, modify, or delete custom tasks and fleet assets.

### 5. Audit Reports & Multi-Format Exporter
- **Complete Audit Trail**: Exports scenario inputs, allocations, deltas, and algorithm explanations.
- **Formats**: Machine-readable **JSON** and tabular **CSV**.
- **Native Android Integration**: One-click clipboard copy and direct sharing via Android System Share Sheet.

---

## 📐 Mathematical Formulation

### Objective Function

$$\min Z = \sum_{i \in \mathcal{T}} \left( w_i \cdot \max(0, t_{i}^{\text{start}} - t_{i}^{\text{earliest}}) + 120 \cdot u_i \right) + 0.1 \sum_{j \in \mathcal{R}} \text{Gap}_j - 50 \sum_{i \in \mathcal{T}_{\text{done}}} w_i$$

Where:
- $\mathcal{T}$: Set of all tasks/missions.
- $\mathcal{R}$: Set of all fleet resources.
- $w_i$: Priority weight ($\text{Critical} = 10, \text{High} = 5, \text{Medium} = 2.5, \text{Low} = 1$).
- $u_i \in \{0, 1\}$: Unscheduled / dropped mission indicator.
- $\text{Gap}_j$: Idle fragmentation window on resource $j$.

### Algorithm Comparison

| Dimension | Baseline (Naive FIFO) | AeroOptima Solver (MIP Heuristic) |
| :--- | :--- | :--- |
| **Strategy** | First-Come-First-Served arrival | Dynamic Priority-Slack Ratio ($w_i / \text{Slack}_i$) |
| **Turnaround Rest** | Greedy first available window | Cooldown-synchronized packing |
| **Priority Handling** | Frequent inversions | Zero inversions (Critical strictly protected) |
| **Disruption Recovery**| Manual / Static cascade delay | Dynamic replanning with minimal schedule churn |
| **Bottleneck Handling**| Silent task drops | Automatic bottleneck detection & mitigation advice |

---

## 🛠️ Architecture & Tech Stack

```
com.example/
├── data/
│   └── SimulationRepository.kt    # State management, scenario store, report generation
├── engine/
│   └── OptimizationEngine.kt      # Mathematical solver, baseline FIFO, bottleneck detection
├── model/
│   ├── Task.kt                    # Task, TaskPriority, MissionType
│   ├── Resource.kt                # Resource, ResourceRole, OperationalStatus
│   ├── Allocation.kt              # AllocationSlot, PlanMetrics, OptimizationComparison
│   └── ScenarioPreset.kt          # SIH-26250 Demo, Surge Crisis, Disruption presets
├── ui/
│   ├── components/
│   │   ├── MetricCard.kt          # High-tech KPI cards with delta badges
│   │   ├── UtilizationChart.kt    # Canvas-rendered fleet load bar chart
│   │   ├── TimelineGanttChart.kt  # Interactive operations Gantt chart
│   │   ├── BottleneckCard.kt      # Warning & feasibility cards
│   │   └── EducationalDisclaimer.kt # SIH compliance banner
│   ├── screens/
│   │   ├── DashboardScreen.kt     # Command overview, before/after metrics
│   │   ├── TimelineScreen.kt      # Operations timeline & constraints audit
│   │   ├── AllocationTableScreen.kt # Filterable task table & XAI rationale
│   │   ├── OptimizerEngineScreen.kt # Math model, objective formula, live logs
│   │   ├── ScenarioLabScreen.kt   # Presets, disruption injector, task creator
│   │   └── ReportScreen.kt        # JSON/CSV exporter & Android share
│   ├── theme/
│   │   ├── Color.kt               # Dark Navy & Cyan Aerospace SaaS palette
│   │   └── Theme.kt               # Material Design 3 configuration
│   └── viewmodel/
│       └── SimulationViewModel.kt # MVVM orchestrator & UI state flows
└── MainActivity.kt                # Top bar, adaptive navigation, BackHandler
```

- **Runtime**: Android SDK (Min SDK 24, Target SDK 36)
- **Language**: Kotlin 2.2.10
- **UI Framework**: Jetpack Compose with Material Design 3 (M3)
- **Architecture**: MVVM + Repository pattern with Kotlin Coroutines and StateFlow
- **Testing**: Robolectric JVM unit testing suite

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio** (Ladybug | 2024.2.1 or newer recommended)
- **JDK 17 or JDK 21**
- **Android SDK API 36**

### Installation & Run

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/AeroOptima-AI.git
   cd AeroOptima-AI
   ```

2. **Open in Android Studio**:
   - Open Android Studio $\rightarrow$ Select `Open an Existing Project` $\rightarrow$ Choose the cloned folder.
   - Wait for Gradle sync to complete.

3. **Run Unit & Optimization Tests**:
   ```bash
   gradle :app:testDebugUnitTest
   ```

4. **Run on Device or Emulator**:
   - Select an Android Virtual Device (AVD) or connected physical device (Android 7.0+ / API 24+).
   - Click **Run** (`Shift + F10`).

---

## 🎤 Hackathon Presentation Guide (3-Minute Pitch)

1. **Minute 1 — The Problem**:  
   *"In high-tempo air operations, allocating heterogeneous assets (fighters, UAVs, airlifts) manually or via simple queuing leads to priority inversions and catastrophic delays when disruptions strike."*
2. **Minute 2 — The Solution**:  
   *Show Dashboard & Timeline: "AeroOptima AI uses mathematical multi-objective constraint optimization to synchronize turnaround times, eliminate priority inversions, and boost efficiency by over 35%."*
3. **Minute 3 — Live Disruption & Explainability**:  
   *Tap 'Scramble QRA' or 'Ground Resource': "Watch the simulator dynamically re-route remaining assets in milliseconds. Open the allocation drawer to see the exact mathematical rationale—demonstrating complete transparency for human-in-the-loop commanders."*

---

## 👥 Hackathon Team & Credits

- **Problem Statement**: SIH26250 — Air Power: Dynamic Air Operations & Resource Optimisation
- **Developed by**: Smart India Hackathon Team
- **Organization**: Ministry / Academic Challenge Prototype

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).
