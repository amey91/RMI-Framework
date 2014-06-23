package communication;

public enum MessageType {
	// used in non-RMI calls from client/server to registry
	LIST, LOOKUP, BIND, REBIND, REMOVE, NONE;
}
