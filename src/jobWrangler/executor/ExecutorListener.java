package jobWrangler.executor;


/**
 * Interface for objects that listen for executor events
 * @author brendanofallon
 *
 */
public interface ExecutorListener {

	public void executorUpdated(ExecutorEvent evt);
}
