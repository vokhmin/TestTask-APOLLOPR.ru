package net.vokhmin.testtask.apollo.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class HomeModel {

	public class StatusByDateOrder implements Comparator<Status> {
		@Override
		public int compare(Status o1, Status o2) {
			assert o1 != null;
			assert o2 != null;
			assert o1.getPublishedAt() != null;
			assert o2.getPublishedAt() != null;
			// reverse order by default
			return o2.getPublishedAt().compareTo(o1.getPublishedAt());
		}
	}

	private Comparator<Status> dateCompatator = new StatusByDateOrder();
	private List<User> users = new ArrayList<User>();
	private List<Status> statuses = new ArrayList<Status>(); // (dateCompatator);

	public List<User> getUsers() {
		return users;
	}

	public List<Status> getStatuses() {
		return statuses;
	}

	public void removeUser(User user) {
		ArrayList<User> rm = new ArrayList<User>();
		for (User u : users) {
			if (u.equals(user)) {
				removeStatuses(u);
				rm.add(u);
			}
		}
		getUsers().removeAll(rm);
	}

	public void removeStatuses(User user) {
		ArrayList<Status> rm = new ArrayList<Status>();
		for (Status s : statuses) {
			if (s.getUser().equals(user)) {
				rm.add(s);
			}
		}
		getStatuses().removeAll(rm);
	}

	public void addStatus(Status status) {
		int i = 0;
		while ((i < statuses.size()) && (dateCompatator.compare(status, statuses.get(i)) >= 0)) {
			i++;
		}
		statuses.add(i, status);
	}
}
