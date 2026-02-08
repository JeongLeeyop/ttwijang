import pymysql

conn = pymysql.connect(
    host='192.168.0.9',
    user='ttwijang',
    password='ttwijang1!',
    database='ttwijang',
    charset='utf8mb4'
)

try:
    with conn.cursor() as cursor:
        # 리그 지역 확인
        cursor.execute("""
            SELECT uid, name, region_sido, region_sigungu, status, current_teams, max_teams
            FROM league 
            WHERE uid LIKE 'league-4817%'
            ORDER BY uid
        """)
        leagues = cursor.fetchall()
        
        print("=== 샘플 리그 현황 ===")
        for league in leagues:
            print(f"{league[1]:30s} | {league[2]:10s} {league[3]:10s} | {league[4]:15s} | {league[5]}/{league[6]} 팀")
        
        # 진주시 IN_PROGRESS 리그 카운트
        cursor.execute("""
            SELECT COUNT(*) 
            FROM league 
            WHERE region_sigungu = '진주시' AND status = 'IN_PROGRESS'
        """)
        jinju_count = cursor.fetchone()[0]
        print(f"\n진주시 진행 중 리그: {jinju_count}개")
        
        # 리그 매치 확인
        cursor.execute("""
            SELECT lm.uid, lm.league_uid, lm.match_date, lm.match_time, lm.status,
                   ht.name as home_team, awt.name as away_team,
                   ht.region_sigungu as home_region, awt.region_sigungu as away_region
            FROM league_match lm
            LEFT JOIN team ht ON lm.home_team_uid = ht.uid
            LEFT JOIN team awt ON lm.away_team_uid = awt.uid
            WHERE lm.league_uid LIKE 'league-4817%'
            ORDER BY lm.match_date, lm.match_time
            LIMIT 5
        """)
        matches = cursor.fetchall()
        
        print("\n=== 리그 매치 샘플 (상위 5개) ===")
        for m in matches:
            print(f"{m[2]} {m[3]} | {m[5]:15s} vs {m[6]:15s} | {m[4]:10s} | 홈:{m[7]} 어웨이:{m[8]}")
        
finally:
    conn.close()
