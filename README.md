# ScopedValue vs ThreadLocal Demo

This project demonstrates the differences between Java's traditional `ThreadLocal` and the new `ScopedValue` (introduced in JDK 21) for managing context in concurrent applications.

## Overview

The demo showcases how both approaches handle request context propagation in a multi-threaded environment, highlighting the advantages of `ScopedValue` over `ThreadLocal`.

Key differences demonstrated:
- Automatic cleanup vs manual cleanup
- Immutability and thread safety
- Explicit scope boundaries
- Context propagation through method calls
- Performance considerations with virtual threads

## Requirements

- JDK 21 or higher
- Maven 3.8.x or higher

## Project Structure

```
src/main/java/com/example/
├── RequestContext.java       # Immutable context record
├── ScopedValuesDemo.java    # Main demo implementation
```

## Building and Running

```bash
mvn clean compile exec:exec
```

## Key Features Demonstrated

### ThreadLocal Approach
- Manual context management with explicit cleanup required
- Risk of memory leaks if cleanup is forgotten
- Mutable state that persists for the entire thread lifetime
- Works with both platform and virtual threads

### ScopedValue Approach
- Automatic cleanup when scope ends
- Immutable context that cannot be modified after setting
- Clear scope boundaries with `where().run()`
- Optimized for virtual threads (Project Loom)
- Safer context propagation

## Output Explanation

The program creates multiple virtual threads to simulate concurrent requests. Each request:
1. Creates a unique context with user and request IDs
2. Logs the context at different stages of execution
3. Demonstrates context access in nested method calls
4. Shows cleanup behavior for both approaches

Sample output format:
```
[ThreadLocal] Starting work with context: RequestContext{userId='user-0', requestId='req-123'}
[ScopedValue] Starting work with context: RequestContext{userId='user-0', requestId='req-456'}
...
```

## Best Practices Highlighted

1. **ThreadLocal**
   - Always clean up in a `finally` block
   - Be cautious with thread pools
   - Consider using try-with-resources pattern

2. **ScopedValue**
   - Use for immutable context
   - Leverage automatic cleanup
   - Take advantage of structured concurrency

## When to Use What

- Use **ScopedValue** when:
  - Working with virtual threads
  - Need immutable context
  - Want automatic cleanup
  - Using structured concurrency

- Use **ThreadLocal** when:
  - Need mutable state
  - Working with older Java versions
  - Require more flexible scope control

## Further Reading

- [JEP 429: Scoped Values (Preview)](https://openjdk.org/jeps/429)
- [Java Virtual Threads](https://docs.oracle.com/en/java/javase/21/core/virtual-threads.html)
- [Thread-Local Variables](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/ThreadLocal.html)
