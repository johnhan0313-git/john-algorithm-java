export interface AuthUser {
  id: number;
  username: string;
  email: string | null;
  display_name: string | null;
  created_at: number;
}

export interface AuthResponse {
  access_token: string;
  token_type: string;
  user: AuthUser;
}

export interface ProblemSummary {
  id: string;
  lc_num: string;
  title: string;
  full_title: string;
  category: string;
  category_label: string;
  difficulty: string;
  difficulty_label: string;
  frequency: string;
  freq_level: string;
  companies: string;
  pass_rate: number;
  pass_rate_text: string;
  fqn: string;
  class_name: string;
  run_command: string;
  summary: string;
  code_lines: number;
  done: boolean;
}

export interface ProblemDetail extends ProblemSummary {
  description: string;
  example: string;
  approach: string;
  notes: string;
  pitfalls: string;
  file_path: string;
  idea_path: string;
  solution_code: string;
}

export interface CategoryMeta {
  key: string;
  label: string;
}

export interface StatsResponse {
  total: number;
  high_freq: number;
  by_difficulty: Record<string, number>;
  by_category: Record<string, number>;
}

export interface ProblemsListResponse {
  items: ProblemSummary[];
  total: number;
  categories: CategoryMeta[];
}

export interface ProgressMapResponse {
  items: Record<string, { slug: string; status: string; notes: string }>;
}
