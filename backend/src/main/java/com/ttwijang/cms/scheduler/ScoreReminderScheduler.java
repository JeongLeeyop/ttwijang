package com.ttwijang.cms.scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ttwijang.cms.api.league.repository.LeagueMatchRepository;
import com.ttwijang.cms.api.match.repository.FutsalMatchRepository;
import com.ttwijang.cms.api.notification.service.NotificationService;
import com.ttwijang.cms.api.team.repository.TeamMemberRepository;
import com.ttwijang.cms.entity.FutsalMatch;
import com.ttwijang.cms.entity.LeagueMatch;
import com.ttwijang.cms.entity.Notification;
import com.ttwijang.cms.entity.TeamMember;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScoreReminderScheduler {

    private final FutsalMatchRepository futsalMatchRepository;
    private final LeagueMatchRepository leagueMatchRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final NotificationService notificationService;

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void sendScoreReminders() {
        sendMatchScoreReminders();
        sendLeagueMatchScoreReminders();
    }

    private void sendMatchScoreReminders() {
        List<FutsalMatch> matches = futsalMatchRepository.findMatchesNeedingScoreReminder();
        for (FutsalMatch match : matches) {
            notifyTeamManagers(
                match.getHostTeamUid(),
                Notification.NotificationType.MATCH,
                "경기 스코어 입력 요청",
                "경기가 종료되었습니다. 스코어를 입력해주세요.",
                match.getUid(),
                "MATCH",
                "/match-detail/" + match.getUid()
            );
            match.setScoreReminderSent(true);
            futsalMatchRepository.save(match);
        }
    }

    private void sendLeagueMatchScoreReminders() {
        List<LeagueMatch> leagueMatches = leagueMatchRepository.findMatchesNeedingScoreReminder();
        for (LeagueMatch match : leagueMatches) {
            String content = "리그 경기가 종료되었습니다. 스코어를 입력해주세요.";
            String actionUrl = "/match-detail/" + match.getUid() + "?type=league&leagueUid=" + match.getLeagueUid();
            notifyTeamManagers(match.getHomeTeamUid(), Notification.NotificationType.LEAGUE,
                "경기 스코어 입력 요청", content, match.getUid(), "LEAGUE_MATCH", actionUrl);
            notifyTeamManagers(match.getAwayTeamUid(), Notification.NotificationType.LEAGUE,
                "경기 스코어 입력 요청", content, match.getUid(), "LEAGUE_MATCH", actionUrl);
            match.setScoreReminderSent(true);
            leagueMatchRepository.save(match);
        }
    }

    private void notifyTeamManagers(String teamUid, Notification.NotificationType type,
                                     String title, String content,
                                     String referenceUid, String referenceType, String actionUrl) {
        List<TeamMember> managers = teamMemberRepository.findByTeamUidAndStatusAndRoleIn(
            teamUid,
            TeamMember.MemberStatus.APPROVED,
            List.of(TeamMember.MemberRole.OWNER, TeamMember.MemberRole.MANAGER)
        );
        for (TeamMember manager : managers) {
            notificationService.createNotification(
                manager.getUserUid(), type, title, content, referenceUid, referenceType, actionUrl
            );
        }
    }
}
